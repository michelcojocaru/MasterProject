package com.thesis.validator.logic;

import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.Feedback;
import com.thesis.validator.enums.SimilarityAlgorithms;
import com.thesis.validator.enums.Tests;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.helpers.Operations;
import com.thesis.validator.model.Node;
import com.thesis.validator.helpers.GraphOperations;
import com.thesis.validator.model.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CouplingChecker extends Checker {

    private static final double COUPLING_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.15;
    private Checker chain;

    // calculate the coefficient of variation of the differences between
    // inward and outward dependencies for each service
    public HashMap<String, TestResult> assessAttribute(List<Service> services,
                                                               List<Relation> relations,
                                                               UseCaseResponsibility useCaseResponsibilities,
                                                               Averages averageType,
                                                               List<SimilarityAlgorithms> algorithms,
                                                               Repo repo) {
        HashMap<String,TestResult> resultScores = new HashMap<>();
        TestResult testResult;
        final int N = services.size();
        double[] couplingScores = new double[N-1]; // disregard the terminal node
        ArrayList<Dependency> dependencies = new ArrayList<>(N);
        int i = 0;
        double result = 0.0;

        for (Service service : services) {
            dependencies.add(new Dependency(service.name, 0, 0));
        }

        Operations.countDependencies(relations, dependencies);

        for (Dependency dependency : dependencies) {
            int in = dependency.getInwardCount();
            int out = dependency.getOutwardCount();

            //couplingScores[i++] = Math.abs(in + out);
            if(out > 0) {
                couplingScores[i++] = out;
            }
        }

        result = Math.abs(MathOperations.getCoefficientOfVariation(couplingScores, averageType) - 1) * 10.0;

        testResult = new TestResult(Tests.DEPENDENCIES_COMPOSITION_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
        Checker.PopulateCauseAndTreatment(testResult,
                Feedback.LOW_CAUSE_DEPENDENCIES.toString(),
                Feedback.LOW_TREATMENT_DEPENDENCIES.toString(),
                Feedback.MEDIUM_CAUSE_DEPENDENCIES.toString(),
                Feedback.MEDIUM_TREATMENT_DEPENDENCIES.toString(),
                Feedback.HIGH_CAUSE_DEPENDENCIES.toString(),
                Feedback.HIGH_TREATMENT_DEPENDENCIES.toString());
        resultScores.put(Tests.DEPENDENCIES_COMPOSITION_TEST.name() ,testResult);

        // SCC identification
        List<List<Node>> components = GraphOperations.searchStronglyConnectedComponents(services,relations);
        String treatment = null;
        result = ((double) components.size()/services.size()) * 10.0;
        for(List<Node> component:components){
            if(component.size() > 1){
                treatment = "We recommend refactoring " + component + " into one microservice!";
            }
        }

        testResult = new TestResult(Tests.SCC_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
        Checker.PopulateCauseAndTreatment(testResult,
                Feedback.LOW_CAUSE_SCC.toString(),
                treatment == null ? Feedback.LOW_TREATMENT_SCC.toString() : treatment,
                Feedback.MEDIUM_CAUSE_SCC.toString(),
                treatment == null ? Feedback.MEDIUM_TREATMENT_SCC.toString() : treatment,
                Feedback.HIGH_CAUSE_SCC.toString(),
                treatment == null ? Feedback.HIGH_TREATMENT_SCC.toString() : treatment);
        resultScores.put(Tests.SCC_TEST.name() ,testResult);

        return resultScores;
    }

}
