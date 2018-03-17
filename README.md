# chat1

## API

### Endpoint Summary

/messages

/messages/{messageId}

/messages/{messageId}/attachment

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
curl http://admin:admin@localhost:18090/generate/10
curl -s http://admin:admin@localhost:18090/messages | jq .
ids=( $(curl -s  -H "Accept: application/json" http://admin:admin@localhost:18090/messages | jq -r .[].id) )
n=${#ids[@]}
idnm2=${ids[$((n-2))]}
data=$(echo "$(date): This is the data stored in the message attachment" |base64)
attjson="{\"data\":\"$data\",\"fileName\":\"fileName\"}"
echo "$attjson" | curl -v -H "Content-Type: application/json" -X PUT \
    -d @- http://admin:admin@localhost:18090/messages/$idnm2/attachment
curl http://admin:admin@localhost:18090/messages/$idnm2/attachment
```