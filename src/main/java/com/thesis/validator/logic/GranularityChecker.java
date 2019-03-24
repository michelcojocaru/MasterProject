package com.thesis.validator.logic;

import com.thesis.validator.enums.Result;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.model.Service;

import java.util.List;

public class GranularityChecker implements CheckerChain {

    private static final double GRANULARITY_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.15;
    private CheckerChain chain;

    // calculate the coefficient of variation between the lengths
    // of nanoentity lists from each service
    private static Result calculateGranularity(List<Service> services) {
        final int N = services.size();
        double[] serviceScores = new double[N];
        double average;
        double standardDeviation;
        double coefficientOfVariation;

        average = MathOperations.calculateAverage(services, serviceScores, N);
        standardDeviation = MathOperations.calculateStandardDeviation(serviceScores);
        coefficientOfVariation = MathOperations.calculateCoefficientOfVariation(average, standardDeviation);

        return coefficientOfVariation < GRANULARITY_COEFFICIENT_OF_VARIATION_THRESHOLD ? Result.passed : Result.failed;
    }

    @Override
    public void setNextChain(CheckerChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public void runAssessment(System system) {
        system.CheckAttribute(calculateGranularity(system.getServices()));

        if (this.chain != null) {
            this.chain.runAssessment(system);
        }
    }
}
