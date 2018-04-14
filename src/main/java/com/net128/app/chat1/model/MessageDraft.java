package com.net128.app.chat1.model;

import io.swagger.annotations.ApiModelProperty;

public class MessageDraft implements JsonObject<MessageDraft> {
    @ApiModelProperty(value = "The recipient id of the message", allowEmptyValue = true, position = 1)
    public String recipientId;
    @ApiModelProperty(value = "The message text", allowEmptyValue = true, position = 2)
    public String text;
    @ApiModelProperty(value = "External API URL which may be rendered within the message.", allowEmptyValue = true, position = 3)
    public String externalUri;

    public Message toMessage() {
        Message message = new Message();
        Payload payload = new Payload();
        payload.text = text;
        payload.externalUri = externalUri;
        message.setPayload(payload);
        message.setRecipientId(recipientId);
        return message;
    }
}
