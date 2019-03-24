package com.thesis.validator.logic;

import com.thesis.validator.enums.Result;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;

import java.util.ArrayList;
import java.util.List;

public class System {

    private List<Result> results;
    private List<Service> services;
    private List<Relation> relations;


    public System(List<Service> services, List<Relation> relations) {
        this.results = new ArrayList<>();
        this.services = services;
        this.relations = relations;
    }

    public List<Service> getServices() {
        return services;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void CheckAttribute(Result result) {
        this.results.add(result);
    }

    public List<Result> getResults() {
        return this.results;
    }
}
