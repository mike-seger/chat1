#!/bin/bash

if [[ ! -x $(which vegeta) || ! -x $(which jaggr) || ! -x $(which jplot) ]] ; then
	echo "OSX only!! Install applications from:"
	echo "https://github.com/tsenart/vegeta"
	exit 1
fi

. $(dirname "$0")/start-app.sh

echo "GET $endpoint" | \
	vegeta attack -rate=$concurrent -duration=${maxtime}s | vegeta dump | \
	jaggr @count=rps hist\[100,200,300,400,500\]:code p25,p50,p95:latency sum:bytes_in sum:bytes_out | \
	jplot rps+code.hist.100+code.hist.200+code.hist.300+code.hist.400+code.hist.500 \
	latency.p95+latency.p50+latency.p25 bytes_in.sum+bytes_out.sum
