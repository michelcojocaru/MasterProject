package com.thesis.validator.logic;

import com.thesis.validator.helpers.Helper;
import com.thesis.validator.model.Dependency;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Checker {

    private static final double GRANULARITY_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.15;
    private static final double COUPLING_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.15;
    private static final double COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.15;

    // calculate the coefficient of variation between the lengths
    // of nanoentity lists from each service
    public static Boolean calculateGranularity(List<Service> services) throws JSONException {
        final int N = services.size();
        int[] serviceScores = new int[N];
        double average;
        double standardDeviation;
        double coefficientOfVariation;

        average = Helper.calculateAverage(services, serviceScores, N);
        standardDeviation = Helper.calculateStandardDeviation(serviceScores);
        coefficientOfVariation = Helper.calculateCoefficientOfVariation(average, standardDeviation);

        return coefficientOfVariation < GRANULARITY_COEFFICIENT_OF_VARIATION_THRESHOLD;
    }

    // calculate the coefficient of variation between the lengths
    // of concepts sets for each service
    public static Boolean calculateCohesion(List<Service> services, List<Relation> relations) throws JSONException {
        final int N = services.size();
        int[] conceptScores = new int[N];
        HashSet<String> concepts;

        for (int i = 0; i < N; i++) {
            concepts = new HashSet<>();
            Service service = services.get(i);
            for(String nanoentity: service.nanoentities){
                String[] tokens = StringUtils.split(nanoentity,".");
                if(tokens != null) {
                    concepts.add(tokens[0]);
                }
            }
            conceptScores[i] = concepts.size();
        }

        //TODO check for duplicates concepts between services ???
        return Helper.getCoefficientOfVariation(N, conceptScores) < COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD;
    }

    // calculate the coefficient of variation of the differences between
    // inward and outward dependencies for each service
    public static Boolean calculateCoupling(List<Service> services, List<Relation> relations) throws JSONException {
        final int N = services.size();
        int[] couplingScores = new int[N];
        ArrayList<Dependency> dependencies = new ArrayList<>(N);

        for (int i = 0; i < services.size(); i++) {
            dependencies.add(new Dependency(services.get(i).name, 0, 0));
        }

        Helper.countDependencies(relations, dependencies);

        for (int i = 0; i < dependencies.size(); i++) {
            int in = dependencies.get(i).getInward();
            int out = dependencies.get(i).getOutward();

            couplingScores[i] = Math.abs(in - out);
        }
        //TODO diferenta in modul normalizat la cea mai mare valoare (look for best practices)
        return Helper.getCoefficientOfVariation(N, couplingScores) < COUPLING_COEFFICIENT_OF_VARIATION_THRESHOLD;
    }


}
