package com.thesis.validator.logic;

import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.Result;
import com.thesis.validator.model.CrystalGlobe;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;
import com.thesis.validator.model.UseCaseResponsibility;

import java.util.List;

public class NewAttributeChecker implements CheckerChain {

    private static final double NEW_ATTRIBUTE_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.0;
    private CheckerChain chain;

    private static Result calculateNewAttribute(List<Service> services, List<Relation> relations, UseCaseResponsibility useCaseResponsibility, Averages averageType) {
        /** Write here the implementation of your quality attribute assessment */

        return Result.passed;
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
