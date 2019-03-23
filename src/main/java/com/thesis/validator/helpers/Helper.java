package com.thesis.validator.helpers;

import com.thesis.validator.enums.Direction;
import com.thesis.validator.model.Dependency;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;
import org.json.JSONException;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Helper {

    private static double average(int[] array) {
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
        for (int num : numArray) {
            sum += num;
        }
        double mean = ((double) sum) / length;
        for (int num : numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    }

    public static double calculateCoefficientOfVariation(double average, double standardDeviation) {
        return standardDeviation / average;
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

    public static Boolean checkForDuplicates(List<Service> services) {
        HashSet<String> entitySet = new HashSet<>();
        HashMap<String, Integer> entityOccurrences = new HashMap<>();

        for (Service service : services) {
            entitySet.clear();
            entitySet = getDistinctEntities(service);
            for (String entity : entitySet) {
                if (!entityOccurrences.containsKey(entity)) {
                    entityOccurrences.put(entity, 1);
                } else {
                    entityOccurrences.put(entity, entityOccurrences.get(entity) + 1);
                }
            }
        }

        for(Integer occurrence: entityOccurrences.values()){
            if(occurrence > 1){
                return true;
            }
        }

        return false;
    }

    public static HashSet<String> getDistinctEntities(Service service) {
        HashSet<String> entities = new HashSet<>();
        for (String nanoentity : service.nanoentities) {
            String[] tokens = StringUtils.split(nanoentity, ".");
            if (tokens != null) {
                entities.add(tokens[0]);
            }
        }
        return entities;
    }
}
