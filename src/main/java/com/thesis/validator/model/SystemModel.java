package com.thesis.validator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.SimilarityAlgorithms;

import java.util.List;

public class SystemModel {

    @JsonProperty("name")
    public String name;
    @JsonProperty("services")
    public List<Service> services;
    @JsonProperty("relations")
    public List<Relation> relations;
    @JsonProperty("useCaseResponsibility")
    public UseCaseResponsibility useCaseResponsibility;
    @JsonProperty("averageType")
    public Averages averageType;
    @JsonProperty("similaritiesAlgorithms")
    public List<SimilarityAlgorithms> algorithms;
    @JsonProperty("repo")
    public Repo repo;
}
