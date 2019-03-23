package com.thesis.validator.helpers;

import com.thesis.validator.enums.Direction;
import com.thesis.validator.model.Dependency;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Helper {

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

        for (Integer occurrence : entityOccurrences.values()) {
            if (occurrence > 1) {
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
