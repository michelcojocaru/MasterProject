package com.thesis.validator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Service {

    @JsonProperty("nanoentities")
    public List<String> nanoentities;
    @JsonProperty("id")
    public String id;
    @JsonProperty("name")
    public String name;
}
