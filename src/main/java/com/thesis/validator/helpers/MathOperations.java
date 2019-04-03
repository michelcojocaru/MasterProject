package com.thesis.validator.helpers;

import com.thesis.validator.enums.Averages;

import java.util.Arrays;

public class MathOperations {

    public static void normalize(double[] array) {
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

    public static double average(double[] array) {
        double average = 0.0;
        for (double elem : array) {
            average += elem;
        }
        average /= array.length;

        return average;
    }

    public static double median(double[] array) {
        double median;

        Arrays.sort(array);
        int len = array.length;
        if (len % 2 == 0) {
            median = (array[len / 2] + array[(len / 2) - 1]) / 2;
        } else {
            median = array[array.length / 2];
        }

        return median;
    }

    private static double calculateStandardDeviation(double[] array) {
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

    private static double calculateCoefficientOfVariation(double average, double standardDeviation) {
        return standardDeviation / average;
    }

    public static double getCoefficientOfVariation(double[] scores, Averages type) {
        int n = scores.length;
        double average;
        double median;
        double standardDeviation;
        double coefficientOfVariation = 0.0;

        standardDeviation = calculateStandardDeviation(scores);


        if (type == Averages.MEDIAN) {
            if (n > 0) {
                median = median(scores);
            } else {
                median = 1;
            }
            coefficientOfVariation = calculateCoefficientOfVariation(median, standardDeviation);

        } else if (type == Averages.MEAN) {
            if (n > 0) {
                average = average(scores);
            } else {
                average = 1;
            }
            coefficientOfVariation = calculateCoefficientOfVariation(average, standardDeviation);
        }

        return coefficientOfVariation;
    }
}
