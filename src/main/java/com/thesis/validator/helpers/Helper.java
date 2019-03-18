package com.thesis.validator.helpers;

import com.thesis.validator.enums.Direction;
import com.thesis.validator.model.Dependency;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Helper {

//    public static ArrayList<String> convertJSONArrayToArrayList(List<String> jsonArray) throws JSONException {
//        ArrayList<String> list = new ArrayList<String>();
//        if (jsonArray != null) {
//            int len = jsonArray.size();
//            for (String aJsonArray : jsonArray) {
//                list.add(aJsonArray);
//            }
//        }
//
//        return list;
//    }

    private static double average(int[] array){
        double average = 0.0;
        for (int elem : array) {
            average += elem;
        }
        average /= array.length;

        return average;
    }

    public static double calculateStandardDeviation(int numArray[]) {
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

    public static double calculateCoefficientOfVariation(double average, double standardDeviation){
        return standardDeviation/average;
    }

    public static double calculateAverage(List<Service> services, int[] serviceScores, int N) throws JSONException {
        double average = 0.0;
        for (int i = 0; i < services.size(); i++) {
            Service service = services.get(i);
            serviceScores[i] = service.nanoentities.size();
        }
        for (int serviceScore : serviceScores) {
            average += serviceScore;
        }
        average /= N;

        return average;
    }

    public static double getCoefficientOfVariation(int n, int[] scores) {
        double average;
        double standardDeviation;
        double coefficientOfVariation;
        if (n > 0) {
            average = Helper.average(scores);
        } else {
            average = 1;
        }
        standardDeviation = Helper.calculateStandardDeviation(scores);
        coefficientOfVariation = Helper.calculateCoefficientOfVariation(average, standardDeviation);

        return coefficientOfVariation;
    }

    public static void countDependencies(List<Relation> relations, ArrayList<Dependency> dependencies) throws JSONException {
        for (Relation relation : relations) {
            if (relation.direction.equals(Direction.INCOMING)) {
                for (Dependency dependency : dependencies) {
                    if (dependency.getServiceName().equals(relation.serviceA)) {
                        dependency.incrementInward();
                    } else if (dependency.getServiceName().equals(relation.serviceB)) {
                        dependency.incrementOutward();
                    }
                }
            } else if (relation.direction.equals(Direction.OUTGOING)) {
                for (Dependency dependency : dependencies) {
                    if (dependency.getServiceName().equals(relation.serviceA)) {
                        dependency.incrementOutward();
                    } else if (dependency.getServiceName().equals(relation.serviceB)) {
                        dependency.incrementInward();
                    }
                }
            }
        }
    }

}
