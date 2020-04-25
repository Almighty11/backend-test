package com.example.backendtest.dtos;

import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class UserRequest {

    @NotNull(message = "ProviderId cannot be null")
    @Range(min = 1)
    private int providerId;

    @NotNull(message = "fields cannot be null")
    private @Valid FieldsRequest[] fields;


    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public FieldsRequest[] getFields() {
        return fields;
    }

    public void setFields(FieldsRequest[] fields) {
        this.fields = fields;
    }
}
