#!/bin/bash

if [[ ! -x locust ]] ; then
	echo "Install applications from:"
	echo "https://docs.locust.io/en/latest/quickstart.html"
	echo "https://microsoft.github.io/PartsUnlimitedMRP/pandp/200.1x-PandP-LocustTest.html"
	exit 1
fi

ulimit -n 2048
ulimit -u 2048
mvn spring-boot:run >/tmp/out.log &

locust -f src/test/scripts/locustfile.py --host=http://server1:18090

# open: http://127.0.0.1:8089/
