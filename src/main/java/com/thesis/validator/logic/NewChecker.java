package com.thesis.validator.logic;

import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.Feedback;
import com.thesis.validator.enums.SimilarityAlgorithms;
import com.thesis.validator.enums.Tests;
import com.thesis.validator.model.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class NewChecker extends Checker {

    private static final double NEW_ATTRIBUTE_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.0;

    public HashMap<String, TestResult> assessAttribute(List<Service> services,
                                                List<Relation> relations,
                                                UseCaseResponsibility useCaseResponsibilities,
                                                Averages averageType,
                                                List<SimilarityAlgorithms> algorithms,
                                                Repo repo) {
        long startTime = System.nanoTime(), endTime, duration;
        HashMap<String,TestResult> resultScores = new HashMap<>();
        TestResult testResult = null;
        double result = 0.0;

        /** Write here
         * the implementation
         * of your quality attribute
         * assessment */

        testResult = new TestResult(Tests.NEWATTRIBUTE_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
        Checker.PopulateCauseAndTreatment(testResult,
                Feedback.LOW_CAUSE_NEWATTRIBUTE.toString(),
                Feedback.LOW_TREATMENT_NEWATTRIBUTE.toString(),
                Feedback.MEDIUM_CAUSE_NEWATTRIBUTE.toString(),
                Feedback.MEDIUM_TREATMENT_NEWATTRIBUTE.toString(),
                Feedback.HIGH_CAUSE_NEWATTRIBUTE.toString(),
                Feedback.HIGH_TREATMENT_NEWATTRIBUTE.toString());
        resultScores.put(testResult.getTestName().name(),testResult);

        endTime = System.nanoTime();
        duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("New Attribute Checker took: " + duration + " nanoseconds.\n--------------------------------------");
        return resultScores;
    }
}
