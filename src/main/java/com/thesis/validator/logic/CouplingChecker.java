package com.thesis.validator.logic;

import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.Mark;
import com.thesis.validator.enums.Tests;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.helpers.Operations;
import com.thesis.validator.model.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CouplingChecker implements CheckerChain {

    private static final double COUPLING_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.15;
    private CheckerChain chain;

    // calculate the coefficient of variation of the differences between
    // inward and outward dependencies for each service
    private static HashMap<String, TestResult> calculateCoupling(List<Service> services, List<Relation> relations, Averages averageType) {
        final int N = services.size();
        double[] couplingScores = new double[N];
        ArrayList<Dependency> dependencies = new ArrayList<>(N);
        int i = 0;

        for (Service service : services) {
            dependencies.add(new Dependency(service.name, 0, 0));
        }

        Operations.countDependencies(relations, dependencies);

        for (Dependency dependency : dependencies) {
            int in = dependency.getInwardCount();
            int out = dependency.getOutwardCount();

            couplingScores[i++] = Math.abs(in - out);
        }

        MathOperations.normalize(couplingScores);

        double result = Math.abs(MathOperations.getCoefficientOfVariation(couplingScores, averageType) - 1) * 10.0;
        HashMap<String,TestResult> resultScores = new HashMap<>();
        TestResult testResult = new TestResult(Tests.COUPLING_COMPOSITION_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
        CheckerChain.PopulateCauseAndTreatment(testResult,
                "We detected an abnormally high variation in inward vs outward dependencies count per microservice!",
                "We recommend revising the dependencies of the system.",
                "We detected an medium variation in inward vs outward dependencies count per microservice!",
                "We recommend revising the use of Bounded Contexts in the design process.",
                "We detected optimal dependencies between the microservices",
                "No need.");
        resultScores.put(Tests.COUPLING_COMPOSITION_TEST.name() ,testResult);

        return resultScores;
    }

    @Override
    public void setNextChain(CheckerChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public void runAssessment(CrystalGlobe crystalGlobe) {
        crystalGlobe.CheckAttribute(this.getClass().getSimpleName(), calculateCoupling(crystalGlobe.getServices(), crystalGlobe.getRelations(), crystalGlobe.getTypeOfAverage()));

        if (this.chain != null) {
            this.chain.runAssessment(crystalGlobe);
        }
    }
}
