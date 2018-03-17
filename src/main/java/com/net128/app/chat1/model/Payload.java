package com.net128.app.chat1.model;

import io.swagger.annotations.ApiModelProperty;

public class Payload implements JsonObject<Payload> {
    @ApiModelProperty(value = "The message text", position = 1)
    public String text;
    @ApiModelProperty(value = "Information about the attachment", position = 2)
    public AttachmentInfo attachmentInfo;
    @ApiModelProperty(value = "External API URL which may be rendered within the message", position = 3)
    public String externalUri;

    public static class AttachmentInfo {
        public String mimeType;
        public String fileName;

        public AttachmentInfo() {}
        public AttachmentInfo(String mimeType, String fileName) {
            this.mimeType = mimeType;
            this.fileName = fileName;
        }
    }

    public Payload(){}
    public Payload(String text) {
        this.text = text;
    }
}
