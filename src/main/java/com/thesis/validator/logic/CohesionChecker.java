package com.thesis.validator.logic;


import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.SimilarityAlgos;
import com.thesis.validator.enums.Tests;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.helpers.NLPOperations;
import com.thesis.validator.helpers.Operations;
import com.thesis.validator.model.CrystalGlobe;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;
import com.thesis.validator.model.UseCaseResponsibility;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CohesionChecker implements CheckerChain {

    private static final double COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.7;
    private CheckerChain chain;

    // calculate the coefficient of variation between the lengths
    // of concepts sets for each service
    private static HashMap<String,Double> calculateCohesion(List<Service> services, List<Relation> relations, UseCaseResponsibility useCaseResponsibilities, Averages averageType, List<SimilarityAlgos> algorithms) {
        final int N = services.size();
        double[] entityScores = new double[N];
        double[] relationScores = new double[relations.size()];
        double[] useCaseResponsibilityScores = new double[N];
        HashSet<String> entities;
        HashMap<String,Double> resultScores = new HashMap<>();
        int i = 0;
        double result = 0.0;

        // Test services property
        if (Operations.checkForDuplicates(services)) {
            //return Result.failed;
            //TODO keep result for pondering it
        }

        for (Service service : services) {
            entities = Operations.getDistinctEntities(service);
            entityScores[i++] = entities.size();
        }
        result = Math.abs(MathOperations.getCoefficientOfVariation(entityScores, averageType) - 1) * 10.0;
        resultScores.put(Tests.ENTITIES_COMPOSITION_TEST.name(),Double.parseDouble(new DecimalFormat(".#").format(result)));

        // Test relations property
        if (Operations.checkForDuplicates(relations)) {
            //return Result.failed;
            //TODO keep result for pondering it
        }
        i = 0;
        for (Relation relation : relations) {
            entities = Operations.getDistinctEntities(relation);
            relationScores[i++] = entities.size();
        }
        result = Math.abs(MathOperations.getCoefficientOfVariation(relationScores, averageType) - 1) * 10.0;
        resultScores.put(Tests.RELATIONS_COMPOSITION_TEST.name(),Double.parseDouble(new DecimalFormat(".#").format(result)));

        // Test useCaseResponsibility property if it exists!
        if(useCaseResponsibilities != null && useCaseResponsibilities.size() != 0) {
            if (Operations.checkForDuplicates(useCaseResponsibilities)) {
                //return Result.failed;
                //TODO keep result for pondering it
            }
            i = 0;
            for (List<String> useCase : useCaseResponsibilities.values()) {
                useCaseResponsibilityScores[i++] = useCase.size();
            }
            result = Math.abs(MathOperations.getCoefficientOfVariation(useCaseResponsibilityScores, averageType) - 1) * 10.0;
            resultScores.put(Tests.RESPONSIBILITIES_COMPOSITION_TEST.name(),Double.parseDouble(new DecimalFormat(".#").format(result)));
        }
        // Test semantic similarity between entities of each service
        for(SimilarityAlgos algorithm:algorithms) {
            result = NLPOperations.checkSemanticSimilarity(services, algorithm);
            resultScores.put(algorithm.name(), Double.parseDouble(new DecimalFormat(".#").format(result)));
        }
        return resultScores;
    }

    @Override
    public void setNextChain(CheckerChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public void runAssessment(CrystalGlobe crystalGlobe) {
        crystalGlobe.CheckAttribute(this.getClass().getSimpleName(), calculateCohesion(crystalGlobe.getServices(), crystalGlobe.getRelations(),
                crystalGlobe.getUseCaseResponsibilities(), crystalGlobe.getTypeOfAverage(), crystalGlobe.getSimilarityAlgorithm()));

        if (this.chain != null) {
            this.chain.runAssessment(crystalGlobe);
        }
    }
}
