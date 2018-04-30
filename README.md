# chat1

## API

### Endpoint Summary

/messages  
/messages/{messageId}  
/messages/{messageId}/attachment  

### Swagger
[Swagger API Documentation](http://localhost:18090/swagger-ui.html)  
[Swagger JSON](http://localhost:18090/develop/v2/api-docs)  
[Swagger yml](http://localhost:18090/develop//swagger.yml)

## H2
[H2 Console](http://localhost:18090/develop/h2_console/)

## HTML pages
[Message Upload Example](http://localhost:18090/develop/index.html)

## Curl examples
```
# generate 10 messages
curl http://user:password@localhost:18090/generate/10

# list all mesaages
curl -s http://user:password@localhost:18090/messages | jq .

# get an array of all message ids
ids=( $(curl -s  -H "Accept: application/json" http://user:password@localhost:18090/chat1/messages | jq -r '.content[]|select(.attachmentInfo) | .id') )

# get an attachment
curl http://user:password@localhost:18090/chat1/messages/${ids[0]}/attachment

# Send and trace message using built-in tcp tunnel on port 18092
echo "{\"firstName\":\"John\",\"lastName\":\"Doe\"}" | gzip | \
    curl -X POST -H "Accept: application/json" \
    -F 'messageDraft={"recipientId":"user","text":"Hello from user","externalUri":"string"};type=application/json' \
    -F "file=@-;filename=file.json.gz" -u user:password \
    "http://localhost:18092/messages"

# Send and trace message using curl trace
curl -X POST ... --trace-ascii - "http://localhost:18090/messages"
    
# Send and trace message using online service    
curl -X POST .... https://httpbin.org/post

# Send and trace message using netcat
nc -l 18080 &
curl -X POST ... "http://user:password@localhost:18080/messages"
```
