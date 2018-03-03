# chat1

## API

### Endpoint Summary

/messages

/messages/{messageId}

/messages/{messageId}/media


## H2 Console
http://localhost:8080/h2_console/

## Curl examples
```
curl http://admin:admin@localhost:8080/generate
curl http://admin:admin@localhost:8080/messages | jq .
id=$(curl -s http://admin:admin@localhost:8080/messages | \
    jq -r .[-1].id)
echo "$(date): strgw trtgw54wergt we" | \
    curl -v -X PUT -T - http://admin:admin@localhost:8080/messages/$id/media
curl http://admin:admin@localhost:8080/messages/$id/media
```