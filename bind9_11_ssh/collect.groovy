import com.jcraft.jsch.JSch
import groovy.json.JsonSlurper
import com.santaba.agent.util.Settings

host = hostProps.get("system.hostname")
user = hostProps.get("ssh.user")
pass = hostProps.get("ssh.pass")
port = hostProps.get("ssh.port") ?: 22
cert = hostProps.get("ssh.cert") ?: '~/.ssh/id_rsa'
timeout = 15000 // timeout in milliseconds

def String sectionHeaderName
def debug = false

def scrapeRE(fullLine, regex, dpname){
  matches = fullLine =~ regex
  if (matches.size() > 0){
    println("${dpname}:${matches[0][1]}")
    return true
  } else {return false}

}



try {
  def output = getCommandOutput("rm -rf /var/cache/bind/named.stats && rndc stats && cat /var/cache/bind/named.stats")
  //do everything in Groovy that the perl script used to do
  output.eachLine{
    line = it.trim()
    matches = line =~ /\+\+\s+(.+)\s+\+\+/
    if (matches.size() > 0){
      if (debug) {println("Found Section: ${matches[0][1]}")}
      sectionHeaderName = matches[0][1]
      return
    } else {
      fullLine = sectionHeaderName + ":" + line
      if (debug) {println(fullLine)}

      if (scrapeRE(fullLine, /Incoming Queries:(\d+)\s+A$/, "ina")){return}
      if (scrapeRE(fullLine, /Incoming Queries:(\d+) NS/, "inns")){return}
      if (scrapeRE(fullLine, /Incoming Queries:(\d+) CNAME/, "incname")){return}
      if (scrapeRE(fullLine, /Incoming Queries:(\d+) SOA/, "insoa")){return}
      if (scrapeRE(fullLine, /Incoming Queries:(\d+) PTR/, "inptr")){return}
      if (scrapeRE(fullLine, /Incoming Queries:(\d+) MX/, "inmx")){return}
      if (scrapeRE(fullLine, /Incoming Queries:(\d+) TXT/, "intxt")){return}
      if (scrapeRE(fullLine, /Incoming Queries:(\d+) AAAA/, "inaaaa")){return}
      if (scrapeRE(fullLine, /Incoming Queries:(\d+) SRV/, "insrv")){return}
      if (scrapeRE(fullLine, /Incoming Queries:(\d+) NAPTR/, "innaptr")){return}
      if (scrapeRE(fullLine, /Incoming Queries:(\d+) A6/, "ina6")){return}
      if (scrapeRE(fullLine, /Incoming Queries:(\d+) SPF/, "inspf")){return}
      if (scrapeRE(fullLine, /Incoming Queries:(\d+) ANY/, "inany")){return}

      if (scrapeRE(fullLine, /Outgoing Queries:(\d+)\s+A$/, "outa")){return}
      if (scrapeRE(fullLine, /Outgoing Queries:(\d+) NS/, "outns")){return}
      if (scrapeRE(fullLine, /Outgoing Queries:(\d+) CNAME/, "outcname")){return}
      if (scrapeRE(fullLine, /Outgoing Queries:(\d+) SOA/, "outsoa")){return}
      if (scrapeRE(fullLine, /Outgoing Queries:(\d+) PTR/, "outptr")){return}
      if (scrapeRE(fullLine, /Outgoing Queries:(\d+) MX/, "outmx")){return}
      if (scrapeRE(fullLine, /Outgoing Queries:(\d+) TXT/, "outtxt")){return}
      if (scrapeRE(fullLine, /Outgoing Queries:(\d+) AAAA/, "outaaaa")){return}
      if (scrapeRE(fullLine, /Outgoing Queries:(\d+) SRV/, "outsrv")){return}
      if (scrapeRE(fullLine, /Outgoing Queries:(\d+) NAPTR/, "outnaptr")){return}
      if (scrapeRE(fullLine, /Outgoing Queries:(\d+) A6/, "outa6")){return}
      if (scrapeRE(fullLine, /Outgoing Queries:(\d+) SPF/, "outspf")){return}
      if (scrapeRE(fullLine, /Outgoing Queries:(\d+) ANY/, "outany")){return}

      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) mismatch responses received/, "rsmismatch")){return}
      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) IPv4 queries sent/, "rsipv4qs")){return}
      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) IPv4 responses received/, "rsipv4rr")){return}
      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) NXDOMAIN received/, "rsnx")){return}
      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) SERVFAIL received/, "rsfail")){return}
      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) FORMERR received/, "rserr")){return}
      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) query retries/, "rsqr")){return}
      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) query timeouts/, "rsqt")){return}
      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) queries with RTT < 10ms/, "rsrtt10")){return}
      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) queries with RTT 10-100ms/, "rsrtt10100")){return}
      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) queries with RTT 100-500ms/, "rsrtt100500")){return}
      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) queries with RTT 500-800ms/, "rsrtt500800")){return}
      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) queries with RTT 800-1600ms/, "rsrtt8001600")){return}
      if (scrapeRE(fullLine, /Resolver Statistics:(\d+) queries with RTT > 1600ms/, "rsrtt1600")){return}

      if (scrapeRE(fullLine, /Socket I\/O Statistics:(\d+) UDP\/IPv4 sockets opened/, "sockopen")){return}
      if (scrapeRE(fullLine, /Socket I\/O Statistics:(\d+) UDP\/IPv4 sockets closed/, "sockclosed")){return}
      if (scrapeRE(fullLine, /Socket I\/O Statistics:(\d+) UDP\/IPv4 socket bind failures/, "sockbf")){return}
      if (scrapeRE(fullLine, /Socket I\/O Statistics:(\d+) UDP\/IPv4 connections established/, "consest")){return}
      if (scrapeRE(fullLine, /Socket I\/O Statistics:(\d+) UDP\/IPv4 recv errors/, "recverr")){return}

    }
  }

  if (debug) {println(output)}
  return 0
}
catch (Exception e) {println "Unexpected Exception : " + e; return 1}

def getCommandOutput(String input_command) {
  try {
    jsch = new JSch()
    if (user && !pass) {jsch.addIdentity(cert)}
    session = jsch.getSession(user, host, port)
    session.setConfig("StrictHostKeyChecking", "no")
    String authMethod = Settings.getSetting(Settings.SSH_PREFEREDAUTHENTICATION, Settings.DEFAULT_SSH_PREFEREDAUTHENTICATION)
    session.setConfig("PreferredAuthentications", authMethod)
    session.setTimeout(timeout)
    if (pass) {session.setPassword(pass)}
    session.connect()
    channel = session.openChannel("exec")
    channel.setCommand(input_command)
    def commandOutput = channel.getInputStream()
    channel.connect()
    def output = commandOutput.text
    channel.disconnect()
    return output
  }
  catch (Exception e) {e.printStackTrace();println("Error in SSH connection.")}
  finally {session.disconnect()}
}