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
