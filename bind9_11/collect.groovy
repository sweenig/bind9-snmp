/*******************************************************************************
 *  © 2007-2019 - LogicMonitor, Inc. All rights reserved.
 ******************************************************************************/
import com.santaba.agent.groovyapi.snmp.Snmp;
hostname = hostProps.get('system.hostname')
props = hostProps.toProperties()
snmp_timeout = 10000
snmp_oid = ".1.3.6.1.4.1.8072.1.3.2.3.1.1.4.98.105.110.100"
data = Snmp.get(hostname, snmp_oid, props, snmp_timeout)
cleaned_data = data.replaceAll(" ","\n")
println(cleaned_data)
return 0