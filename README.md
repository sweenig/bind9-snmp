# Polling Bind9 DNS Statistics via SSH
If the SNMP method is not preferred, the [bind9_11_ssh.xml](../blob/master/bind9_11_ssh.xml) DataSource will poll via SSH instead. You'll need to set ssh.user and ssh.pass as properties on the device through the normal mechanisms. That user will need root level permissions. That can be through sudo as long as the sudo password is not prompted. The user needs the ability to run the following commands without being prompted for an elevated permissions password (edit the sudoers file if necessary):
- rm -rf /var/cache/bind/named.stats
- rndc stats
- cat /var/cache/bind/named.stats | /etc/snmp/scripts/dnsstats.pl

# Polling BIND9 DNS Statistics via SNMP
Execution by root of `rndc stats` on the DNS server must create the output file at /var/cache/bind/named.stats.  This allows the statistics to be output by BIND9.

The line:
```
extend bind /etc/snmp/scripts/runstats.sh
```
must be added to the snmpd.conf file (usually located at /etc/snmp/snmpd.conf). This tells SNMP to execute the script when the OID corresponding to the "bind" extension (.1.3.6.1.4.1.8072.1.3.2.3.1.1.4.98.105.110.100).  
Tip: 98.105.110.100 corresponds to ASCII characters "b.i.n.d". The "4" that preceeds them corresponds to the extension index.

The two scripts here must be copied into /etc/snmp/scripts/.
runstats.sh deletes the existing file, asks BIND9 to regenerate the statistics file, then pipes that file into dnsstats.pl, which parses the file into comma separated key value pairs.  That string of key value pairs is then returned to snmpd which returns it as the response to the SnmpGet command.

# Some resources used in developing this:
- https://www.packetmischief.ca/monitoring-bind9/
- http://www.net-snmp.org/docs/man/snmpd.conf.html
- https://geekpeek.net/extend-snmp-run-bash-scripts-via-snmp/
- https://www.maketecheasier.com/add-remove-user-to-groups-in-ubuntu/
- https://www.youtube.com/watch?v=SATEOZwjw4U
- http://www.whiteboardcoder.com/2017/11/install-and-setup-bind9-server-on.html
<br /><a href="http://www.youtube.com/watch?feature=player_embedded&v=SATEOZwjw4U
" target="_blank"><img src="http://img.youtube.com/vi/SATEOZwjw4U/0.jpg"
alt="IMAGE ALT TEXT HERE" width="240" height="180" border="10" /></a>
