#!/usr/bin/perl
  use strict;
  use warnings;
  my %sections = (
      "Incoming Queries" => 1,
      "Outgoing Queries" => 1,
      "Resolver Statistics" => 1,
      "Socket I/O Statistics" => 1,
  );
#loop variables
  my ($active,$new_line);
#incoming queries
  my ($ina,$inns,$incname,$insoa,$inptr,$inmx,$intxt,$inaaaa,$insrv,$innaptr,$ina6,$inspf,$inany);
#outgoing queries
  my ($outa,$outns,$outcname,$outsoa,$outptr,$outmx,$outtxt,$outaaaa,$outsrv,$outnaptr,$outa6,$outspf,$outany);
#resolver statistics
  my ($rsnx,$rsfail,$rserr,$rsmismatch,$rsipv4qs,$rsipv4rr,$rsqr,$rsqt,$rsrtt10,$rsrtt10100,$rsrtt100500,$rsrtt500800,$rsrtt8001600,$rsrtt1600);
#i/o stats
  my ($sockopen,$sockclosed,$sockbf,$consest,$recverr);

  while(my $line = <STDIN>) {
    $line =~ s/^\s+//; #remove space at the beginning of the line
    $line =~ s/\s+$//; #remove space at the end of the line
    if ($line =~ /^\+\+\s+(.+)\s+\+\+$/) { $active = $1; } #look for section headers in the format of "++ SECTION ++"
    if ($active and exists $sections{$active}) {$new_line=$active.":".$line."\n"}

    if ($new_line=~ /Incoming Queries:(\d+)\s+A$/){ $ina=$1; }
    if ($new_line=~ /Incoming Queries:(\d+) NS/){ $inns=$1; }
    if ($new_line=~ /Incoming Queries:(\d+) CNAME/){ $incname=$1; }
    if ($new_line=~ /Incoming Queries:(\d+) SOA/){ $insoa=$1; }
    if ($new_line=~ /Incoming Queries:(\d+) PTR/){ $inptr=$1; }
    if ($new_line=~ /Incoming Queries:(\d+) MX/){ $inmx=$1; }
    if ($new_line=~ /Incoming Queries:(\d+) TXT/){ $intxt=$1; }
    if ($new_line=~ /Incoming Queries:(\d+) AAAA/){ $inaaaa=$1; }
    if ($new_line=~ /Incoming Queries:(\d+) SRV/){ $insrv=$1; }
    if ($new_line=~ /Incoming Queries:(\d+) NAPTR/){ $innaptr=$1; }
    if ($new_line=~ /Incoming Queries:(\d+) A6/){ $ina6=$1; }
    if ($new_line=~ /Incoming Queries:(\d+) SPF/){ $inspf=$1; }
    if ($new_line=~ /Incoming Queries:(\d+) ANY/){ $inany=$1; }

    if ($new_line=~ /Outgoing Queries:(\d+)\s+A$/){ $outa=$1; }
    if ($new_line=~ /Outgoing Queries:(\d+) NS/){ $outns=$1; }
    if ($new_line=~ /Outgoing Queries:(\d+) CNAME/){ $outcname=$1; }
    if ($new_line=~ /Outgoing Queries:(\d+) SOA/){ $outsoa=$1; }
    if ($new_line=~ /Outgoing Queries:(\d+) PTR/){ $outptr=$1; }
    if ($new_line=~ /Outgoing Queries:(\d+) MX/){ $outmx=$1; }
    if ($new_line=~ /Outgoing Queries:(\d+) TXT/){ $outtxt=$1; }
    if ($new_line=~ /Outgoing Queries:(\d+) AAAA/){ $outaaaa=$1; }
    if ($new_line=~ /Outgoing Queries:(\d+) SRV/){ $outsrv=$1; }
    if ($new_line=~ /Outgoing Queries:(\d+) NAPTR/){ $outnaptr=$1; }
    if ($new_line=~ /Outgoing Queries:(\d+) A6/){ $outa6=$1; }
    if ($new_line=~ /Outgoing Queries:(\d+) SPF/){ $outspf=$1; }
    if ($new_line=~ /Outgoing Queries:(\d+) ANY/){ $outany=$1; }

    if ($new_line=~ /Resolver Statistics:(\d+) mismatch responses received/){ $rsmismatch=$1; }
    if ($new_line=~ /Resolver Statistics:(\d+) IPv4 queries sent/){ $rsipv4qs=$1; }
    if ($new_line=~ /Resolver Statistics:(\d+) IPv4 responses received/){ $rsipv4rr=$1; }
    if ($new_line=~ /Resolver Statistics:(\d+) NXDOMAIN received/){ $rsnx=$1; }
    if ($new_line=~ /Resolver Statistics:(\d+) SERVFAIL received/){ $rsfail=$1; }
    if ($new_line=~ /Resolver Statistics:(\d+) FORMERR received/){ $rserr=$1; }
    if ($new_line=~ /Resolver Statistics:(\d+) query retries/){ $rsqr=$1; }
    if ($new_line=~ /Resolver Statistics:(\d+) query timeouts/){ $rsqt=$1; }
    if ($new_line=~ /Resolver Statistics:(\d+) queries with RTT < 10ms/){ $rsrtt10=$1; }
    if ($new_line=~ /Resolver Statistics:(\d+) queries with RTT 10-100ms/){ $rsrtt10100=$1; }
    if ($new_line=~ /Resolver Statistics:(\d+) queries with RTT 100-500ms/){ $rsrtt100500=$1; }
    if ($new_line=~ /Resolver Statistics:(\d+) queries with RTT 500-800ms/){ $rsrtt500800=$1; }
    if ($new_line=~ /Resolver Statistics:(\d+) queries with RTT 800-1600ms/){ $rsrtt8001600=$1; }
    if ($new_line=~ /Resolver Statistics:(\d+) queries with RTT > 1600ms/){ $rsrtt1600=$1; }

    if ($new_line=~ /Socket I\/O Statistics:(\d+) UDP\/IPv4 sockets opened/){ $sockopen=$1; }
    if ($new_line=~ /Socket I\/O Statistics:(\d+) UDP\/IPv4 sockets closed/){ $sockclosed=$1; }
    if ($new_line=~ /Socket I\/O Statistics:(\d+) UDP\/IPv4 socket bind failures/){ $sockbf=$1; }
    if ($new_line=~ /Socket I\/O Statistics:(\d+) UDP\/IPv4 connections established/){ $consest=$1; }
    if ($new_line=~ /Socket I\/O Statistics:(\d+) UDP\/IPv4 recv errors/){ $recverr=$1; }
  }

print "ina:".$ina." ina6:".$ina6." inaaaa:".$inaaaa." inany:".$inany." incname:".$incname." inmx:".$inmx." innaptr:".$innaptr." inns:".$inns." inptr:".$inptr." insoa:".$insoa." inspf:".$inspf." insrv:".$insrv." intxt:".$intxt." rsnx:".$rsnx." outa:".$outa." outa6:".$outa6." outaaaa:".$outaaaa." outany:".$outany." outcname:".$outcname." outmx:".$outmx." outnaptr:".$outnaptr." outns:".$outns." outptr:".$outptr." outsoa:".$outsoa." outspf:".$outspf." outsrv:".$outsrv." outtxt:".$outtxt." rsnx:".$rsnx." rsfail:".$rsfail." rserr:".$rserr." rsipv4qs:".$rsipv4qs." rsipv4rr:".$rsipv4rr." rsmismatch:".$rsmismatch." rsqr:".$rsqr." rsqt:".$rsqt." rsrtt10:".$rsrtt10." rsrtt100500:".$rsrtt100500." rsrtt10100:".$rsrtt10100." rsrtt1600:".$rsrtt1600." rsrtt500800:".$rsrtt500800." rsrtt8001600:".$rsrtt8001600." sockopen:".$sockopen." sockclosed:".$sockclosed." sockbf:".$sockbf." consest:".$consest." recverr:".$recverr;
