package com.thesis.validator.logic;

import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.Tests;
import com.thesis.validator.model.CrystalGlobe;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;
import com.thesis.validator.model.UseCaseResponsibility;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class NewAttributeChecker implements CheckerChain {

    private static final double NEW_ATTRIBUTE_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.0;
    private CheckerChain chain;

    private static HashMap<String,Double> calculateNewAttribute(List<Service> services, List<Relation> relations, UseCaseResponsibility useCaseResponsibility, Averages averageType) {
        HashMap<String,Double> resultScores = new HashMap<>();
        /** Write here
         * the implementation
         * of your quality attribute
         * assessment */
        resultScores.put(Tests.NEWATTRIBUTE_TEST.name(), Double.parseDouble(new DecimalFormat(".#").format(0.0)));
        return resultScores;
    }

    @Override
    public void setNextChain(CheckerChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public void runAssessment(CrystalGlobe crystalGlobe) {
        crystalGlobe.CheckAttribute(this.getClass().getSimpleName(), calculateNewAttribute(crystalGlobe.getServices(),
                crystalGlobe.getRelations(), crystalGlobe.getUseCaseResponsibilities(), crystalGlobe.getTypeOfAverage()));

        if (this.chain != null) {
            this.chain.runAssessment(crystalGlobe);
        }
    }
}
