#!/bin/sh

rm -rf /var/cache/bind/named.stats
rndc stats
cat /var/cache/bind/named.stats | /etc/snmp/scripts/dnsstats.pl
