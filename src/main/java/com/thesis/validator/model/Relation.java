package com.thesis.validator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thesis.validator.enums.Direction;

import java.util.List;

public class Relation {

    @JsonProperty("serviceA")
    public String serviceA;
    @JsonProperty("serviceB")
    public String serviceB;
    @JsonProperty("sharedEntities")
    public List<String> sharedEntities;
    @JsonProperty("direction")
    public Direction direction;
}
