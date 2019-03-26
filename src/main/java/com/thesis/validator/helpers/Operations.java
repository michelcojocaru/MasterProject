package com.thesis.validator.helpers;

import com.thesis.validator.enums.Direction;
import com.thesis.validator.model.Dependency;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;
import com.thesis.validator.model.UseCaseResponsibility;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Operations {

    public static void countDependencies(List<Relation> relations, ArrayList<Dependency> dependencies) {

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

    public static <T> Boolean checkForDuplicates(List<T> items) {
        HashSet<String> entitySet = new HashSet<>();
        HashMap<String, Integer> entityOccurrences = new HashMap<>();

        for (T t : items) {
            entitySet.clear();
            entitySet = getDistinctEntities(t);
            for (String entity : entitySet) {
                if (!entityOccurrences.containsKey(entity)) {
                    entityOccurrences.put(entity, 1);
                } else {
                    entityOccurrences.put(entity, entityOccurrences.get(entity) + 1);
                }
            }
        }

        for (Integer occurrence : entityOccurrences.values()) {
            if (occurrence > 1) {
                return true;
            }
        }

        return false;
    }

    public static Boolean checkForDuplicates(UseCaseResponsibility useCaseResponsibility) {
        HashMap<String, Integer> useCaseOccurrences = new HashMap<>();

        for (List<String> useCases : useCaseResponsibility.values()) {
            for (String useCase : useCases) {
                if (!useCaseOccurrences.containsKey(useCase)) {
                    useCaseOccurrences.put(useCase, 1);
                } else {
                    useCaseOccurrences.put(useCase, useCaseOccurrences.get(useCase) + 1);
                }
            }
        }

        for (Integer occurrence : useCaseOccurrences.values()) {
            if (occurrence > 1) {
                return true;
            }
        }

        return false;
    }

    public static <T> HashSet<String> getDistinctEntities(T t) {
        HashSet<String> entities = new HashSet<>();

        if (t instanceof Service) {
            for (String nanoentity : ((Service) t).nanoentities) {
                String[] tokens = StringUtils.split(nanoentity, ".");
                if (tokens != null) {
                    entities.add(tokens[0]);
                }
            }
        } else if (t instanceof Relation) {
            for (String sharedEntity : ((Relation) t).sharedEntities) {
                String[] tokens = StringUtils.split(sharedEntity, ".");
                if (tokens != null) {
                    entities.add(tokens[0]);
                }
            }
        }

        return entities;
    }
}
