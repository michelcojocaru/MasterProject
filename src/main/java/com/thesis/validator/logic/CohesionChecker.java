package com.thesis.validator.logic;


import com.thesis.validator.enums.Result;
import com.thesis.validator.helpers.Helper;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;

import java.util.HashSet;
import java.util.List;

public class CohesionChecker implements CheckerChain {

    private static final double COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.15;
    private CheckerChain chain;

    // calculate the coefficient of variation between the lengths
    // of concepts sets for each service
    private static Result calculateCohesion(List<Service> services, List<Relation> relations) {
        final int N = services.size();
        double[] entityScores = new double[N];
        HashSet<String> entities;
        int i = 0;

        if (Helper.checkForDuplicates(services)) {
            return Result.failed;
        }

        for (Service service : services) {
            entities = Helper.getDistinctEntities(service);
            entityScores[i++] = entities.size();
        }

        //TODO implement NLP strategy
        //double similarity = NLP.calculateSemanticSimilarity("article", "booking");

        return MathOperations.getCoefficientOfVariation(N, entityScores) < COHESION_COEFFICIENT_OF_VARIATION_THRESHOLD ? Result.passed : Result.failed;
    }

    @Override
    public void setNextChain(CheckerChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public void runAssessment(System system) {
        system.CheckAttribute(calculateCohesion(system.getServices(), system.getRelations()));

        if (this.chain != null) {
            this.chain.runAssessment(system);
        }
    }
}
