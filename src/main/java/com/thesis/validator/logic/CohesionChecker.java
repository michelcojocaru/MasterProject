package com.thesis.validator.logic;


import com.thesis.validator.enums.Result;
import com.thesis.validator.helpers.Helper;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;
import com.thesis.validator.model.UseCaseResponsibility;

import java.util.HashSet;
import java.util.List;

public class CohesionChecker implements CheckerChain {

    private static final double COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.7;
    private CheckerChain chain;

    // calculate the coefficient of variation between the lengths
    // of concepts sets for each service
    private static Result calculateCohesion(List<Service> services, List<Relation> relations, UseCaseResponsibility useCaseResponsibilities) {
        final int N = services.size();
        double[] entityScores = new double[N];
        double[] relationScores = new double[relations.size()];
        double[] useCaseResponsibilityScores = new double[N];
        HashSet<String> entities;
        boolean servicesTest;
        boolean relationsTest;
        boolean userResponsibilityTest;
        int i = 0;

        // Test services property
        if (Helper.checkForDuplicates(services)) {
            return Result.failed;
        }

        for (Service service : services) {
            entities = Helper.getDistinctEntities(service);
            entityScores[i++] = entities.size();
        }
        servicesTest = MathOperations.getCoefficientOfVariation(N, entityScores) < COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD;

        // Test relations property
        if (Helper.checkForDuplicates(relations)) {
            return Result.failed;
        }
        i = 0;
        for (Relation relation : relations) {
            entities = Helper.getDistinctEntities(relation);
            relationScores[i++] = entities.size();
        }
        relationsTest = MathOperations.getCoefficientOfVariation(N, relationScores) < COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD;

        // Test useCaseResponsibility property
        if (Helper.checkForDuplicates(useCaseResponsibilities)) {
            return Result.failed;
        }
        i = 0;
        for (List<String> useCase : useCaseResponsibilities.values()) {
            useCaseResponsibilityScores[i++] = useCase.size();
        }

        userResponsibilityTest = MathOperations.getCoefficientOfVariation(N, useCaseResponsibilityScores) < COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD;

        //TODO implement NLP strategy
        //double similarity = NLP.calculateSemanticSimilarity("article", "booking");

        return (servicesTest && relationsTest && userResponsibilityTest) ? Result.passed : Result.failed;
    }

    @Override
    public void setNextChain(CheckerChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public void runAssessment(System system) {
        system.CheckAttribute(calculateCohesion(system.getServices(), system.getRelations(), system.getUseCaseResponsibilities()));

        if (this.chain != null) {
            this.chain.runAssessment(system);
        }
    }
}
