package com.thesis.validator.logic;

import com.thesis.validator.helpers.Helper;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.helpers.NLP;
import com.thesis.validator.model.Dependency;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Checker {

    private static final double GRANULARITY_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.15;
    private static final double COUPLING_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.15;
    private static final double COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.15;

    // calculate the coefficient of variation between the lengths
    // of nanoentity lists from each service
    public static Boolean calculateGranularity(List<Service> services) {
        final int N = services.size();
        double[] serviceScores = new double[N];
        double average;
        double standardDeviation;
        double coefficientOfVariation;

        average = MathOperations.calculateAverage(services, serviceScores, N);
        standardDeviation = MathOperations.calculateStandardDeviation(serviceScores);
        coefficientOfVariation = MathOperations.calculateCoefficientOfVariation(average, standardDeviation);

        return coefficientOfVariation < GRANULARITY_COEFFICIENT_OF_VARIATION_THRESHOLD;
    }

    // calculate the coefficient of variation between the lengths
    // of concepts sets for each service
    public static Boolean calculateCohesion(List<Service> services, List<Relation> relations) {
        final int N = services.size();
        double[] entityScores = new double[N];
        HashSet<String> entities;
        int i = 0;

        if(Helper.checkForDuplicates(services)){
            return false;
        }

        for (Service service: services) {
            entities = Helper.getDistinctEntities(service);
            entityScores[i++] = entities.size();
        }

        //TODO implement NLP strategy
        NLP.calculateSemanticSimilarity("article", "booking");

        return MathOperations.getCoefficientOfVariation(N, entityScores) < COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD;
    }

    // calculate the coefficient of variation of the differences between
    // inward and outward dependencies for each service
    public static Boolean calculateCoupling(List<Service> services, List<Relation> relations) throws JSONException {
        final int N = services.size();
        double[] couplingScores = new double[N];
        ArrayList<Dependency> dependencies = new ArrayList<>(N);
        int i = 0;

        for (Service service: services) {
            dependencies.add(new Dependency(service.name, 0, 0));
        }

        Helper.countDependencies(relations, dependencies);

        for (Dependency dependency: dependencies) {
            int in = dependency.getInwardCount();
            int out = dependency.getOutwardCount();

            couplingScores[i++] = Math.abs(in - out);
        }

        MathOperations.normalizeAtHighestValue(couplingScores);

        return MathOperations.getCoefficientOfVariation(N, couplingScores) < COUPLING_COEFFICIENT_OF_VARIATION_THRESHOLD;
    }
}
