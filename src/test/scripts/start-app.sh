concurrent=1000
maxrequests=50000

if [ $# == 2 ] ; then
    concurrent=$1
    maxrequests=$2
fi

maxtime=$((maxrequests / concurrent))

port=20090
baseurl="http://user:password@localhost:$port"
endpoint="$baseurl/messages"

unameOut="$(uname -s)"
case "${unameOut}" in
    Linux*)     os=Linux;;
    Darwin*)    os=Mac;;
    CYGWIN*)    os=Cygwin;;
    MINGW*)     os=MinGw;;
    *)          os="UNKNOWN:${unameOut}"
esac
echo ${machine}

curlopts=""
if [ $os == Mac ] ; then
    curlopts="--retry-connrefused"
    sudo sysctl -w kern.maxfiles=25000 >/dev/null
    sudo sysctl -w kern.maxfilesperproc=24500 >/dev/null
    sudo sysctl -w kern.ipc.somaxconn=20000 >/dev/null
    ulimit -S -n 20000 >/dev/null
elif [ $os == Mac ] ; then
    ulimit -S -n 20000 >/dev/null
fi

mvn spring-boot:run -Dspring-boot.run.profiles='perftest' >/tmp/out.log &
pid=$!
echo "server running under pid $pid"

function finish() {
    kill $pid $(jobs -p)
}

trap finish EXIT

echo " "
echo "-------------------------------------------------------------"
echo ""
echo "Starting Application on $endpoint ..."

curl -s --connect-timeout 1 $curlopts \
    --max-time 1 \
    --retry 10 \
    --retry-delay 1 \
    --retry-max-time 17 \
    "$endpoint" 2>&1>/dev/null
echo "stage 1: waiting for status code 200.."
bash -c 'while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' '"$endpoint"')" != "200" ]]; do sleep 0.5; done'
echo "stage 2: generating 100 messages"
curl -s $baseurl/generate/100 >/dev/null

echo -n "Application started: "
date -u +"%Y-%m-%dT%H:%M:%SZ"
echo ""
echo "Preparing Tests..."
bench-rest -n 5000 -c $concurrent "$endpoint" > /dev/null 2>&1
echo "Preparation done."
echo "Starting Tests... (concurrent=$concurrent, maxrequests=$maxrequests, maxtime=$maxtime)"
echo " "
