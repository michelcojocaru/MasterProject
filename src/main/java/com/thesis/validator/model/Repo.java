package com.thesis.validator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Repo {

    @JsonProperty("url")
    public String url;
    @JsonProperty("languages")
    public List<String> languages;
}
