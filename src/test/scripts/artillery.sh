#!/bin/bash

if [[ ! -x $(which artillery) ]] ; then
	echo "Install applications from:"
	echo "https://artillery.io/docs/getting-started/"
	exit 1
fi

. $(dirname "$0")/start-app.sh

artillery quick -d $maxtime -r $concurrent $endpoint
#artillery quick --count 100 -n 200 http://user:password@localhost:18090/messages
