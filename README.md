# chat1

## API

### Endpoint Summary

/messages

/messages/{messageId}

/messages/{messageId}/data

## Swagger API Documentation
http://localhost:18090/swagger-ui.html
### Swagger JSON
http://localhost:18090/v2/api-docs
### Swagger yml
http://localhost:18090//swagger.yml

## H2 Console
http://localhost:18090/h2_console/

## Curl examples
```
curl -X POST http://admin:admin@localhost:18090/generate/10
curl http://admin:admin@localhost:18090/messages | jq .
ids=( $(curl -s http://admin:admin@localhost:18090/messages | jq -r .[].id) )
n=${#ids[@]}
idnm2=${ids[$((n-2))]}
echo "$(date): This is the data stored in the message attachment" | \
    curl -v -X PUT -T - http://admin:admin@localhost:18090/messages/$idnm2/data
curl http://admin:admin@localhost:18090/messages/$idnm2/data
```