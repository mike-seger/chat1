package com.net128.app.chat1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.validation.constraints.Size;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@MappedSuperclass
public class Identifiable {
    @Id
    @JsonProperty(access = READ_ONLY)
    @Column(length = 32)
    @Size(max = 32)
    @ApiModelProperty(value = "The unique id (uuid)", position = 1)
    private String id;

    @PrePersist
    protected void initId() {
        id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
