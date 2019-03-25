package com.thesis.validator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SystemModel {

    @JsonProperty("services")
    public List<Service> services;
    @JsonProperty("relations")
    public List<Relation> relations;
    @JsonProperty("useCaseResponsibility")
    public UseCaseResponsibility useCaseResponsibility;
}
