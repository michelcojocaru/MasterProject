package com.thesis.validator.logic;


import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.Feedback;
import com.thesis.validator.enums.SimilarityAlgorithms;
import com.thesis.validator.enums.Tests;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.helpers.NLPOperations;
import com.thesis.validator.helpers.Operations;
import com.thesis.validator.model.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CohesionChecker extends Attribute {

    private static final double COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.7;
    private Attribute chain;

    // calculate the coefficient of variation between the lengths
    // of concepts sets for each service
    public HashMap<String,TestResult> assessAttribute(List<Service> services,
                                                              List<Relation> relations,
                                                              UseCaseResponsibility useCaseResponsibilities,
                                                              Averages averageType,
                                                              List<SimilarityAlgorithms> algorithms,
                                                              Repo repo) {
        final int N = services.size();
        double[] entityScores = new double[N];
        double[] relationScores = new double[relations.size()];
        double[] useCaseResponsibilityScores = new double[N];
        HashSet<String> entities;
        HashMap<String, TestResult> resultScores = new HashMap<>();
        TestResult testResult;
        int i = 0;
        double result = 0.0;

        // Test services property
        if (Operations.checkForDuplicates(services)) {
            testResult = new TestResult(Tests.ENTITIES_COMPOSITION_TEST, Double.parseDouble(new DecimalFormat(".#").format(0.0)));
            Attribute.PopulateCauseAndTreatment(testResult,
                    Feedback.LOW_CAUSE_ENTITIES_DUPLICATES.toString(),
                    Feedback.LOW_TREATMENT_ENTITIES_DUPLICATES.toString(),
                    Feedback.MEDIUM_CAUSE_ENTITIES_DUPLICATES.toString(),
                    Feedback.MEDIUM_TREATMENT_ENTITIES_DUPLICATES.toString(),
                    Feedback.HIGH_CAUSE_ENTITIES_DUPLICATES.toString(),
                    Feedback.HIGH_TREATMENT_ENTITIES_DUPLICATES.toString());
        } else {
            for (Service service : services) {
                entities = Operations.getEntities(service, true);
                entityScores[i++] = entities.size();
            }
            MathOperations.normalize(entityScores);
            result = Math.abs(MathOperations.getCoefficientOfVariation(entityScores, averageType) - 1) * 10.0;
            testResult = new TestResult(Tests.ENTITIES_COMPOSITION_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
            Attribute.PopulateCauseAndTreatment(testResult,
                    Feedback.LOW_CAUSE_ENTITIES_COMPOSITION.toString(),
                    Feedback.LOW_TREATMENT_ENTITIES_COMPOSITION.toString(),
                    Feedback.MEDIUM_CAUSE_ENTITIES_COMPOSITION.toString(),
                    Feedback.MEDIUM_TREATMENT_ENTITIES_COMPOSITION.toString(),
                    Feedback.HIGH_CAUSE_ENTITIES_COMPOSITION.toString(),
                    Feedback.HIGH_TREATMENT_ENTITIES_COMPOSITION.toString());
        }
        resultScores.put(Tests.ENTITIES_COMPOSITION_TEST.name(),testResult);

        // Test relations property
        if (Operations.checkForDuplicates(relations)) {
            testResult = new TestResult(Tests.RELATIONS_COMPOSITION_TEST, Double.parseDouble(new DecimalFormat(".#").format(0.0)));
            Attribute.PopulateCauseAndTreatment(testResult,
                    Feedback.LOW_CAUSE_RELATIONS_DUPLICATES.toString(),
                    Feedback.LOW_TREATMENT_RELATIONS_DUPLICATES.toString(),
                    Feedback.MEDIUM_CAUSE_RELATIONS_DUPLICATES.toString(),
                    Feedback.MEDIUM_TREATMENT_RELATIONS_DUPLICATES.toString(),
                    Feedback.HIGH_CAUSE_RELATIONS_DUPLICATES.toString(),
                    Feedback.HIGH_TREATMENT_RELATIONS_DUPLICATES.toString());
        }else {
            i = 0;
            for (Relation relation : relations) {
                entities = Operations.getEntities(relation, true);
                relationScores[i++] = entities.size();
            }
            result = Math.abs(MathOperations.getCoefficientOfVariation(relationScores, averageType) - 1) * 10.0;
            testResult = new TestResult(Tests.RELATIONS_COMPOSITION_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
            Attribute.PopulateCauseAndTreatment(testResult,
                    Feedback.LOW_CAUSE_RELATIONS_COMPOSITION.toString(),
                    Feedback.LOW_TREATMENT_RELATIONS_COMPOSITION.toString(),
                    Feedback.MEDIUM_CAUSE_RELATIONS_COMPOSITION.toString(),
                    Feedback.MEDIUM_TREATMENT_RELATIONS_COMPOSITION.toString(),
                    Feedback.HIGH_CAUSE_RELATIONS_COMPOSITION.toString(),
                    Feedback.HIGH_TREATMENT_RELATIONS_COMPOSITION.toString());
        }

        resultScores.put(testResult.getTestName().name(),testResult);

        // Test useCaseResponsibility property if provided! [optional parameter]
        if(useCaseResponsibilities != null && useCaseResponsibilities.size() != 0) {
            if (Operations.checkForDuplicates(useCaseResponsibilities)) {
                testResult = new TestResult(Tests.RESPONSIBILITIES_COMPOSITION_TEST, Double.parseDouble(new DecimalFormat(".#").format(0.0)));
                Attribute.PopulateCauseAndTreatment(testResult,
                        Feedback.LOW_CAUSE_RESPONSIBILITIES_DUPLICATES.toString(),
                        Feedback.LOW_TREATMENT_RESPONSIBILITIES_DUPLICATES.toString(),
                        Feedback.MEDIUM_CAUSE_RESPONSIBILITIES_DUPLICATES.toString(),
                        Feedback.MEDIUM_TREATMENT_RESPONSIBILITIES_DUPLICATES.toString(),
                        Feedback.HIGH_CAUSE_RESPONSIBILITIES_DUPLICATES.toString(),
                        Feedback.HIGH_TREATMENT_RESPONSIBILITIES_DUPLICATES.toString());
            }else {
                i = 0;
                for (List<String> useCase : useCaseResponsibilities.values()) {
                    useCaseResponsibilityScores[i++] = useCase.size();
                }
                result = Math.abs(MathOperations.getCoefficientOfVariation(useCaseResponsibilityScores, averageType) - 1) * 10.0;
                testResult = new TestResult(Tests.RESPONSIBILITIES_COMPOSITION_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));

                Attribute.PopulateCauseAndTreatment(testResult,
                        Feedback.LOW_CAUSE_RESPONSIBILITIES_COMPOSITION.toString(),
                        Feedback.LOW_TREATMENT_RESPONSIBILITIES_COMPOSITION.toString(),
                        Feedback.MEDIUM_CAUSE_RESPONSIBILITIES_COMPOSITION.toString(),
                        Feedback.MEDIUM_TREATMENT_RESPONSIBILITIES_COMPOSITION.toString(),
                        Feedback.HIGH_CAUSE_RESPONSIBILITIES_COMPOSITION.toString(),
                        Feedback.HIGH_TREATMENT_RESPONSIBILITIES_COMPOSITION.toString());
            }

            resultScores.put(testResult.getTestName().name(),testResult);
        }

        // Test semantic similarity between entities of each service using multiple algorithms
        for(SimilarityAlgorithms algorithm:algorithms) {
            result = NLPOperations.checkSemanticSimilarity(services, algorithm);

            testResult = new TestResult(Tests.SEMANTIC_SIMILARITY_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
            Attribute.PopulateCauseAndTreatment(testResult,
                    Feedback.LOW_CAUSE_SEMANTIC_SIMILARITY.toString(),
                    Feedback.LOW_TREATMENT_SEMANTIC_SIMILARITY.toString(),
                    Feedback.MEDIUM_CAUSE_SEMANTIC_SIMILARITY.toString(),
                    Feedback.MEDIUM_TREATMENT_SEMANTIC_SIMILARITY.toString(),
                    Feedback.HIGH_CAUSE_SEMANTIC_SIMILARITY.toString(),
                    Feedback.HIGH_TREATMENT_SEMANTIC_SIMILARITY.toString());
            resultScores.put(algorithm.name(),testResult);
        }
        return resultScores;
    }

}
