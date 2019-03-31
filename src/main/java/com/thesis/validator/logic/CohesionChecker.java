package com.thesis.validator.logic;


import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.Result;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.helpers.NLPOperations;
import com.thesis.validator.helpers.Operations;
import com.thesis.validator.model.CrystalGlobe;
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
    private static Result calculateCohesion(List<Service> services, List<Relation> relations, UseCaseResponsibility useCaseResponsibilities, Averages averageType) {
        final int N = services.size();
        double[] entityScores = new double[N];
        double[] relationScores = new double[relations.size()];
        double[] useCaseResponsibilityScores = new double[N];
        HashSet<String> entities;
        boolean servicesCompositionTest;
        boolean similarityTest;
        boolean relationsCompositionTest;
        //boolean relationsSimilarityTest;
        boolean userResponsibilityCompositionTest;
        //boolean userResponsibilitySimilarityTest;
        int i = 0;

        // Test services property
        if (Operations.checkForDuplicates(services)) {
            return Result.failed;
        }

        for (Service service : services) {
            entities = Operations.getDistinctEntities(service);
            entityScores[i++] = entities.size();
        }
        servicesCompositionTest = MathOperations.getCoefficientOfVariation(entityScores, averageType) < COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD;

        // Test relations property
        if (Operations.checkForDuplicates(relations)) {
            return Result.failed;
        }
        i = 0;
        for (Relation relation : relations) {
            entities = Operations.getDistinctEntities(relation);
            relationScores[i++] = entities.size();
        }
        relationsCompositionTest = MathOperations.getCoefficientOfVariation(relationScores, averageType) < COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD;

        // Test useCaseResponsibility property
        if (Operations.checkForDuplicates(useCaseResponsibilities)) {
            return Result.failed;
        }
        i = 0;
        for (List<String> useCase : useCaseResponsibilities.values()) {
            useCaseResponsibilityScores[i++] = useCase.size();
        }

        userResponsibilityCompositionTest = MathOperations.getCoefficientOfVariation(useCaseResponsibilityScores, averageType) < COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD;

        // Test semantic similarity between entities of each service
        similarityTest = NLPOperations.checkSemanticSimilarity(services);

        return (servicesCompositionTest && relationsCompositionTest && userResponsibilityCompositionTest && similarityTest) ? Result.passed : Result.failed;
    }

    @Override
    public void setNextChain(CheckerChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public void runAssessment(CrystalGlobe crystalGlobe) {
        crystalGlobe.CheckAttribute(this.getClass().getSimpleName(), calculateCohesion(crystalGlobe.getServices(), crystalGlobe.getRelations(),
                crystalGlobe.getUseCaseResponsibilities(), crystalGlobe.getTypeOfAverage()));

        if (this.chain != null) {
            this.chain.runAssessment(crystalGlobe);
        }
    }
}
