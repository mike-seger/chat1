#!/bin/bash

if [[ ! -x vegeta || ! jaggr || ! jplot  ]] ; then
	echo "Install applications from:"
	echo "https://github.com/tsenart/vegeta"
	exit 1
fi

ulimit -n 2048
ulimit -u 2048
mvn spring-boot:run >/tmp/out.log &

echo "GET http://user:password@localhost:18090/messages" | \
	vegeta attack -rate=1000 -duration=120s | vegeta dump | \
	jaggr @count=rps hist\[100,200,300,400,500\]:code p25,p50,p95:latency sum:bytes_in sum:bytes_out | \
	jplot rps+code.hist.100+code.hist.200+code.hist.300+code.hist.400+code.hist.500 \
	latency.p95+latency.p50+latency.p25 bytes_in.sum+bytes_out.sum
