#!/bin/bash

if [[ ! -x $(which locust) ]] ; then
	echo "Install applications from:"
	echo "https://docs.locust.io/en/latest/quickstart.html"
	echo "https://microsoft.github.io/PartsUnlimitedMRP/pandp/200.1x-PandP-LocustTest.html"
	exit 1
fi

. $(dirname "$0")/start-app.sh

locust -f src/test/scripts/locustfile.py --host="$baseurl" \
    --no-web --clients=$concurrent --hatch-rate=$concurrent  --num-request=$maxrequests

#locust -f src/test/scripts/locustfile.py --host="$baseurl"
# open: http://127.0.0.1:8089/
