# chat1

## H2 Console
http://localhost:8080/h2_console/

## Curl examples
```
curl http://admin:admin@localhost:8080/generate
curl http://admin:admin@localhost:8080/find-all | jq .
id=$(curl -s http://admin:admin@localhost:8080/find-all | \
    jq -r .[-1].id)
echo "$(date): strgw trtgw54wergt we" | \
    curl -v -X PUT -T - http://admin:admin@localhost:8080/data/$id
curl http://admin:admin@localhost:8080/data/$id
```