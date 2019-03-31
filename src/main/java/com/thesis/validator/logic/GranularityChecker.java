package com.thesis.validator.logic;

import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.Tests;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.helpers.Operations;
import com.thesis.validator.model.CrystalGlobe;
import com.thesis.validator.model.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GranularityChecker implements CheckerChain {

    private static final double GRANULARITY_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.5;
    private CheckerChain chain;

    // calculate the coefficient of variation between the lengths
    // of nanoentity lists from each service
    private static HashMap<String,Double> calculateGranularity(List<Service> services, Averages averageType) {
        final int N = services.size();
        double[] serviceScores = new double[N];
        HashSet<String> entities;
        int i = 0;

        for (Service service : services) {
            entities = Operations.getDistinctEntities(service);
            serviceScores[i++] = entities.size();
        }

        MathOperations.normalize(serviceScores);

        double result = Math.abs(MathOperations.getCoefficientOfVariation(serviceScores, averageType) - 1) * 10.0;
        HashMap<String, Double> resultScores = new HashMap<>();
        resultScores.put(Tests.NANOENTITIES_COMPOSITION_TEST.name(), Double.parseDouble(new DecimalFormat(".#").format(result)));

        return resultScores;
    }

    @Override
    public void setNextChain(CheckerChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public void runAssessment(CrystalGlobe crystalGlobe) {
        crystalGlobe.CheckAttribute(this.getClass().getSimpleName(), calculateGranularity(crystalGlobe.getServices(), crystalGlobe.getTypeOfAverage()));

        if (this.chain != null) {
            this.chain.runAssessment(crystalGlobe);
        }
    }
}
