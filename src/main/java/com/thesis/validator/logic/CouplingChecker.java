package com.thesis.validator.logic;

import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.Feedback;
import com.thesis.validator.enums.SimilarityAlgorithms;
import com.thesis.validator.enums.Tests;
import com.thesis.validator.helpers.CSVOperations;
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
    public HashMap<String, TestResult> assessAttribute(String systemName,
                                                       List<Service> services,
                                                               List<Relation> relations,
                                                               UseCaseResponsibility useCaseResponsibilities,
                                                               Averages averageType,
                                                               List<SimilarityAlgorithms> algorithms,
                                                               Repo repo) {
        long startCheckerTime = System.nanoTime(), endCheckerTime;
        long startTestTime, endTestTime;
        long duration;
        HashMap<String,TestResult> resultScores = new HashMap<>();
        TestResult testResult;
        final int N = services.size();
        double[] couplingScores = new double[N]; // disregard the terminal node N-1
        ArrayList<Dependency> dependencies = new ArrayList<>(N);
        int i = 0;
        double result = 0.0;
        boolean sccDetected = false;

        if(relations != null) {
            startTestTime = System.nanoTime();
            testResult = new TestResult(Tests.DEPENDENCIES_COMPOSITION_TEST);
            for (Service service : services) {
                dependencies.add(new Dependency(service.name, 0, 0));
            }

            Operations.countDependencies(relations, dependencies);

            for (Dependency dependency : dependencies) {
                int out = dependency.getOutwardCount();
                couplingScores[i++] = out;
                Checker.PopulateDetails(testResult, dependency.getServiceName(), String.valueOf(out), "Service", "outward dependencies");
            }

            if (Operations.areDependenciesBalanced(couplingScores)) {
                result = 10.0;
            } else {
                result = Math.abs(MathOperations.getCoefficientOfVariation(couplingScores, averageType) - 1) * 10.0;
            }

            testResult.setScore(Double.parseDouble(new DecimalFormat(".#").format(result)));
            Checker.PopulateCauseAndTreatment(testResult,
                    Feedback.LOW_CAUSE_DEPENDENCIES.toString(),
                    Feedback.LOW_TREATMENT_DEPENDENCIES.toString(),
                    Feedback.MEDIUM_CAUSE_DEPENDENCIES.toString(),
                    Feedback.MEDIUM_TREATMENT_DEPENDENCIES.toString(),
                    Feedback.HIGH_CAUSE_DEPENDENCIES.toString(),
                    Feedback.HIGH_TREATMENT_DEPENDENCIES.toString());
            resultScores.put(Tests.DEPENDENCIES_COMPOSITION_TEST.name(), testResult);

            endTestTime = System.nanoTime();
            duration = (endTestTime - startTestTime);
            System.out.print(Tests.DEPENDENCIES_COMPOSITION_TEST.toString() + "," + duration + ",");
            CSVOperations.logRunTimes(systemName, new String[]{ Tests.DEPENDENCIES_COMPOSITION_TEST.toString() , String.valueOf(duration)});
            //System.out.println("DEPENDENCIES_COMPOSITION_TEST took: " + duration + " nanoseconds.");

            // SCC identification
            startTestTime = System.nanoTime();
            testResult = new TestResult(Tests.SCC_TEST);
            List<List<Node>> components = GraphOperations.searchStronglyConnectedComponents(services, relations);
            result = ((double) components.size() / services.size()) * 10.0;
            testResult.setScore(Double.parseDouble(new DecimalFormat(".#").format(result)));
            for (List<Node> component : components) {
                if (component.size() > 1) {
                    Checker.PopulateDetails(testResult, component.toString(), null, "Services", "to be grouped into one microservice");
                    sccDetected = true;
                }
            }
            if (!sccDetected) {
                testResult.setDetails("No details.");
            }
            Checker.PopulateCauseAndTreatment(testResult,
                    Feedback.LOW_CAUSE_SCC.toString(),
                    Feedback.LOW_TREATMENT_SCC.toString(),
                    Feedback.MEDIUM_CAUSE_SCC.toString(),
                    Feedback.MEDIUM_TREATMENT_SCC.toString(),
                    Feedback.HIGH_CAUSE_SCC.toString(),
                    Feedback.HIGH_TREATMENT_SCC.toString());
            resultScores.put(Tests.SCC_TEST.name(), testResult);

            endTestTime = System.nanoTime();
            duration = (endTestTime - startTestTime);
            System.out.print(Tests.SCC_TEST.toString() + "," + duration + ",");
            CSVOperations.logRunTimes(systemName, new String[]{ Tests.SCC_TEST.toString() , String.valueOf(duration)});
            //System.out.println("SCC_TEST took: " + duration + " nanoseconds.");
        }

        endCheckerTime = System.nanoTime();
        duration = (endCheckerTime - startCheckerTime);
        //System.out.println("CouplingChecker took: " + duration + " nanoseconds.");

        return resultScores;
    }

}
