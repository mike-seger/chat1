#!/bin/bash

if [[ ! -x artillery  ]] ; then
	echo "Install applications from:"
	echo "https://artillery.io/docs/getting-started/"
	exit 1
fi

ulimit -n 2048
ulimit -u 2048
mvn spring-boot:run >/tmp/out.log &

artillery quick -d 60 -r 500 http://user:password@server1:18090/messages
#artillery quick --count 100 -n 200 http://user:password@localhost:18090/messages
