package com.net128.app.chat1.model;

import io.swagger.annotations.ApiModelProperty;

public class Payload implements JsonObject<Payload> {
    @ApiModelProperty(value = "The message text", position = -60, allowEmptyValue = true)
    public String text;
    @ApiModelProperty(value = "Information about the attachment. (automatically set when file content is attached)", position = -59, readOnly = true)
    public AttachmentInfo attachmentInfo;
    @ApiModelProperty(value = "External API URL which may be rendered within the message.", position = -58, allowEmptyValue = true)
    public String externalUri;

    public static class AttachmentInfo {
        @ApiModelProperty(value = "The mime type of the attached file content. This value is determined automatically.", readOnly = true)
        public String mimeType;
        @ApiModelProperty(value = "The file name of the uploaded file. This value is set by the API client when the file is attached.", readOnly = true)
        public String fileName;
        @ApiModelProperty(value = "The size of the attached file content. This value is determined automatically.", readOnly = true)
        public long size;

        public AttachmentInfo() {}
        public AttachmentInfo(String mimeType, String fileName, long size) {
            this.mimeType = mimeType;
            this.fileName = fileName;
            this.size = size;
        }
    }

    public Payload(){}
    public Payload(String text) {
        this.text = text;
    }
}
