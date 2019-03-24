package com.thesis.validator.logic;

import com.thesis.validator.enums.Result;
import com.thesis.validator.helpers.Helper;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.model.Dependency;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;

import java.util.ArrayList;
import java.util.List;

public class CouplingChecker implements CheckerChain {

    private static final double COUPLING_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.15;
    private CheckerChain chain;

    // calculate the coefficient of variation of the differences between
    // inward and outward dependencies for each service
    private static Result calculateCoupling(List<Service> services, List<Relation> relations) {
        final int N = services.size();
        double[] couplingScores = new double[N];
        ArrayList<Dependency> dependencies = new ArrayList<>(N);
        int i = 0;

        for (Service service : services) {
            dependencies.add(new Dependency(service.name, 0, 0));
        }

        Helper.countDependencies(relations, dependencies);

        for (Dependency dependency : dependencies) {
            int in = dependency.getInwardCount();
            int out = dependency.getOutwardCount();

            couplingScores[i++] = Math.abs(in - out);
        }

        MathOperations.normalizeAtHighestValue(couplingScores);

        return MathOperations.getCoefficientOfVariation(N, couplingScores) < COUPLING_COEFFICIENT_OF_VARIATION_THRESHOLD ? Result.passed : Result.failed;
    }

    @Override
    public void setNextChain(CheckerChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public void runAssessment(System system) {
        system.CheckAttribute(calculateCoupling(system.getServices(), system.getRelations()));

        if (this.chain != null) {
            this.chain.runAssessment(system);
        }
    }
}
