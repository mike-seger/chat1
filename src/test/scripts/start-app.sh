concurrent=1000
maxrequests=50000
maxtime=$((maxrequests / concurrent))

if [ $# == 3 ] ; then
    concurrent=$1
    maxrequests=$2
    maxtime=$3
fi

port=20090
baseurl="http://user:password@localhost:$port"
endpoint="$baseurl/messages"

sudo sysctl -w kern.maxfiles=25000 >/dev/null
sudo sysctl -w kern.maxfilesperproc=24500 >/dev/null
sudo sysctl -w kern.ipc.somaxconn=20000 >/dev/null
ulimit -S -n 20000 >/dev/null
mvn spring-boot:run \
    -Drun.jvmArguments="
        -Dserver.port=$port
        -Dspring.datasource.url=jdbc:h2:~/h2/chat1-test;MODE=MySQL;DB_CLOSE_ON_EXIT=TRUE
        -Dspring.jpa.hibernate.ddl-auto=create
     " \
    >/tmp/out.log &

trap 'kill $(jobs -p)' EXIT

echo " "
echo "-------------------------------------------------------------"
echo ""
echo "Starting Application..."

curl -s --connect-timeout 1 \
    --max-time 1 \
    --retry-connrefused \
    --retry 10 \
    --retry-delay 1 \
    --retry-max-time 17 \
    "$endpoint" 2>&1>/dev/null
bash -c 'while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' '"$endpoint"')" != "200" ]]; do sleep 0.5; done'
curl -s $baseurl/generate/100 >/dev/null

echo -n "Application started: "
date -u +"%Y-%m-%dT%H:%M:%SZ"
echo ""
echo "Preparing Tests..."
bench-rest -n 5000 -c $concurrent "$endpoint" > /dev/null 2>&1
echo "Preparation done."
echo "Starting Tests... (concurrent=$concurrent, maxrequests=$maxrequests, maxtime=$maxtime)"
echo " "

