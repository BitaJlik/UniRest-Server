package com.unirest.core.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

public interface IToken {
    @JsonIgnore
    String getSubject();

    @JsonIgnore
    default String getIssuer() {
        return "UniRestServer";
    }

    @JsonIgnore
    HashMap<String, ?> getValues();
}