package com.thesis.validator.helpers;

import com.thesis.validator.model.Service;

import java.util.List;

public class MathOperations {

    public static void normalizeAtHighestValue(double[] array) {
        double max = getMaxValue(array);
        for (int i = 0; i < array.length; i++) {
            if (max > 0) {
                array[i] /= max;
            }
        }
    }

    private static double getMaxValue(double[] array) {
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    private static double average(double[] array) {
        double average = 0.0;
        for (double elem : array) {
            average += elem;
        }
        average /= array.length;

        return average;
    }

    public static double calculateStandardDeviation(double[] array) {
        int sum = 0;
        double standardDeviation = 0.0;
        int length = array.length;

        for (double num : array) {
            sum += num;
        }
        double mean = ((double) sum) / length;
        for (double num : array) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    }

    public static double calculateCoefficientOfVariation(double average, double standardDeviation) {
        return standardDeviation / average;
    }

    public static double calculateAverage(List<Service> services, double[] scores, int N) {
        double average = 0.0;

        for (int i = 0; i < services.size(); i++) {
            Service service = services.get(i);
            scores[i] = service.nanoentities.size();
        }
        for (double score : scores) {
            average += score;
        }
        average /= N;

        return average;
    }

    public static double getCoefficientOfVariation(int n, double[] scores) {
        double average;
        double standardDeviation;
        double coefficientOfVariation;

        if (n > 0) {
            average = average(scores);
        } else {
            average = 1;
        }

        standardDeviation = calculateStandardDeviation(scores);
        coefficientOfVariation = calculateCoefficientOfVariation(average, standardDeviation);

        return coefficientOfVariation;
    }
}
