#!/bin/bash

if [[ ! -x $(which bench-rest) ]] ; then
	echo "Install applications from:"
	echo "https://github.com/jeffbski/bench-rest"
	exit 1
fi

. $(dirname "$0")/start-app.sh

bench-rest -n $maxrequests -c $concurrent "$endpoint"
