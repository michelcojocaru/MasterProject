package com.thesis.validator.model;
import com.thesis.validator.enums.Averages;

import java.util.HashMap;
import java.util.List;

public class CrystalGlobe {

    private HashMap<String, HashMap<String, Double>> results;
    private List<Service> services;
    private List<Relation> relations;
    private UseCaseResponsibility useCaseResponsibilities;
    private Averages averageType;


    public CrystalGlobe(List<Service> services, List<Relation> relations, UseCaseResponsibility useCaseResponsibilities, Averages averageType) {
        this.results = new HashMap<>();
        this.services = services;
        this.relations = relations;
        this.useCaseResponsibilities = useCaseResponsibilities;
        this.averageType = averageType;
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

    public void CheckAttribute(String testName, HashMap<String,Double> result) {
        this.results.put(testName, result);
    }

    public HashMap<String, HashMap<String,Double>> getResults() {
        return this.results;
    }

    public Averages getTypeOfAverage() {
        return this.averageType;
    }
}
