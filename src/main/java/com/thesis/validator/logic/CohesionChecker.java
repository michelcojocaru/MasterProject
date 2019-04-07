package com.thesis.validator.logic;


import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.SimilarityAlgos;
import com.thesis.validator.enums.Tests;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.helpers.NLPOperations;
import com.thesis.validator.helpers.Operations;
import com.thesis.validator.model.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CohesionChecker implements CheckerChain {

    private static final double COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.7;
    private CheckerChain chain;

    // calculate the coefficient of variation between the lengths
    // of concepts sets for each service
    private static HashMap<String,TestResult> calculateCohesion(List<Service> services, List<Relation> relations, UseCaseResponsibility useCaseResponsibilities, Averages averageType, List<SimilarityAlgos> algorithms) {
        final int N = services.size();
        double[] entityScores = new double[N];
        double[] relationScores = new double[relations.size()];
        double[] useCaseResponsibilityScores = new double[N];
        HashSet<String> entities;
        HashMap<String, TestResult> resultScores = new HashMap<>();
        TestResult resultObject;
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
        TestResult testResult = new TestResult(Tests.ENTITIES_COMPOSITION_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
        CheckerChain.PopulateCauseAndTreatment(testResult,
                "We detected an abnormally high variation in inward vs outward dependencies count per microservice!",
                "We recommend revising the dependencies of the system.",
                "We detected an medium variation in inward vs outward dependencies count per microservice!",
                "We recommend revising the use of Bounded Contexts in the design process.",
                "We detected optimal dependencies between the microservices",
                "No need.");
        resultScores.put(Tests.ENTITIES_COMPOSITION_TEST.name(),testResult);

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
        testResult = new TestResult(Tests.RELATIONS_COMPOSITION_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
        CheckerChain.PopulateCauseAndTreatment(testResult,
                "Low Cause related info",
                "Low Treatment related info",
                " Mid Cause related info",
                "Mid Treatment related info",
                "High Cause related info",
                "High Treatment related info");
        resultScores.put(testResult.getTestName().name(),testResult);

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
            testResult = new TestResult(Tests.RESPONSIBILITIES_COMPOSITION_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
            CheckerChain.PopulateCauseAndTreatment(testResult,
                    "Low Cause related info",
                    "Low Treatment related info",
                    " Mid Cause related info",
                    "Mid Treatment related info",
                    "High Cause related info",
                    "High Treatment related info");
            resultScores.put(testResult.getTestName().name(),testResult);
        }
        // Test semantic similarity between entities of each service using multiple algorithms
        for(SimilarityAlgos algorithm:algorithms) {
            result = NLPOperations.checkSemanticSimilarity(services, algorithm);

            testResult = new TestResult(Tests.SEMANTIC_SIMILARITY_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
            CheckerChain.PopulateCauseAndTreatment(testResult,
                    "Low Cause related info",
                    "Low Treatment related info",
                    " Mid Cause related info",
                    "Mid Treatment related info",
                    "High Cause related info",
                    "High Treatment related info");
            resultScores.put(algorithm.name(),testResult);
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
