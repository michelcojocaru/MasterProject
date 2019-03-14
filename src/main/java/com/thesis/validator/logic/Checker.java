package com.thesis.validator.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Checker {

    private static final double COEFFICIENT_OF_VARIATION_THRESHOLD = 0.15;

    private static double calculateStandardDeviation(int numArray[]) {
        int sum = 0;
        double standardDeviation = 0.0;
        int length = numArray.length;
        for(int num : numArray) {
            sum += num;
        }
        double mean = ((double)sum)/length;
        for(int num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

    private static double calculateCoeficientOfVariation(double average, double standardDeviation){
        return standardDeviation/average;
    }

    private static double calculateAverage(JSONArray services, int[] serviceScores, int N) throws JSONException {
        double average = 0.0;
        for (int i = 0; i < services.length(); i++) {
            JSONObject service = services.getJSONObject(i);
            serviceScores[i] = service.getJSONArray("nanoentities").length();
        }
        for (int serviceScore : serviceScores) {
            average += serviceScore;
        }
        average /= N;

        return average;
    }

    public static Boolean calculateGranularity(JSONArray services) throws JSONException {
        int N = services.length();
        int[] serviceScores = new int[N];
        double average = 0.0;
        double standardDeviation = 0.0;
        double coefficientOfVariation = 0.0;

        average = calculateAverage(services, serviceScores, N);
        standardDeviation = calculateStandardDeviation(serviceScores);
        coefficientOfVariation = calculateCoeficientOfVariation(average, standardDeviation);

        return coefficientOfVariation < COEFFICIENT_OF_VARIATION_THRESHOLD;
    }

    public static Boolean calculateCohesion(JSONArray relations) {
        return false;
    }

    public static Boolean calculateCoupling(JSONArray relations) {
        return false;
    }
}
