package com.unirest.data.models;

import lombok.Data;

@Data
public class ErrorMessage {
    private String username;
    private String errorTitle;
    private String[] errorDescription;
    private String sdkVersion;
}