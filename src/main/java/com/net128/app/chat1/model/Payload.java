package com.net128.app.chat1.model;

import io.swagger.annotations.ApiModelProperty;

public class Payload implements JsonObject<Payload> {
    @ApiModelProperty(value = "The message text", position = 1, required = false)
    public String text;
    @ApiModelProperty(value = "Information about the attachment. (automatically set when file content is attached)", position = 2, required = false)
    public AttachmentInfo attachmentInfo;
    @ApiModelProperty(value = "External API URL which may be rendered within the message.", position = 3, required = false)
    public String externalUri;

    public static class AttachmentInfo {
        @ApiModelProperty(value = "The mime type of the attached file content. This value is determined automatically.")
        public String mimeType;
        @ApiModelProperty(value = "The file name of the uploaded file. This value is set by the API client when the file is attached.")
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
