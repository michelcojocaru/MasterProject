package com.thesis.validator.model;
import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.SimilarityAlgorithms;

import java.util.HashMap;
import java.util.List;

public class CrystalGlobe {

    private HashMap<String, HashMap<String, TestResult>> results;
    private List<Service> services;
    private List<Relation> relations;
    private UseCaseResponsibility useCaseResponsibilities;
    private Averages averageType;
    private List<SimilarityAlgorithms> algorithms;


    public CrystalGlobe(List<Service> services, List<Relation> relations, UseCaseResponsibility useCaseResponsibilities, Averages averageType, List<SimilarityAlgorithms> algorithms) {
        this.results = new HashMap<>();
        this.services = services;
        this.relations = relations;
        this.useCaseResponsibilities = useCaseResponsibilities;
        this.averageType = averageType;
        this.algorithms = algorithms;
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

    public void CheckAttribute(String testName, HashMap<String,TestResult> result) {
        this.results.put(testName, result);
    }

    public HashMap<String, HashMap<String,TestResult>> getResults() {
        return this.results;
    }

    public Averages getTypeOfAverage() {
        return this.averageType;
    }

    public List<SimilarityAlgorithms> getSimilarityAlgorithm() {
        return this.algorithms;
    }
}
