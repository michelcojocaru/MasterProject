package com.thesis.validator.logic;

import com.thesis.validator.enums.Result;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;
import com.thesis.validator.model.UseCaseResponsibility;

import java.util.ArrayList;
import java.util.List;

public class System {

    private List<Result> results;
    private List<Service> services;
    private List<Relation> relations;
    private UseCaseResponsibility useCaseResponsibilities;


    public System(List<Service> services, List<Relation> relations, UseCaseResponsibility useCaseResponsibilities) {
        this.results = new ArrayList<>();
        this.services = services;
        this.relations = relations;
        this.useCaseResponsibilities = useCaseResponsibilities;
    }

    public List<Service> getServices() {
        return services;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public UseCaseResponsibility getUseCaseResponsibilities() {
        return useCaseResponsibilities;
    }

    public void CheckAttribute(Result result) {
        this.results.add(result);
    }

    public List<Result> getResults() {
        return this.results;
    }
}
