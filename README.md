# chat1

## API

### Endpoint Summary

/messages  
/messages/{messageId}  
/messages/{messageId}/attachment  

### Swagger
[Swagger API Documentation](http://localhost:18090/swagger-ui.html)  
[Swagger JSON](http://localhost:18090/v2/api-docs)  
[Swagger yml](http://localhost:18090//swagger.yml)

## H2
[H2 Console](http://localhost:18090/h2_console/)

## HTML pages
[Message Upload Example](http://localhost:18090/develop/index.html)

## Curl examples
```
curl http://user:password@localhost:18090/generate/10
curl -s http://user:password@localhost:18090/messages | jq .
ids=( $(curl -s  -H "Accept: application/json" http://user:password@localhost:18090/messages | jq -r '.content[]|select(.attachmentInfo) | .id') )
curl http://user:password@localhost:18090/messages/${ids[0]}/attachment
```