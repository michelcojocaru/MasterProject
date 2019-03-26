package com.thesis.validator.logic;

import com.thesis.validator.enums.Result;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;
import com.thesis.validator.model.UseCaseResponsibility;

import java.util.List;

public class NewAttributeChecker implements CheckerChain {

    private static final double NEW_ATTRIBUTE_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.0;
    private CheckerChain chain;

    private static Result calculateNewAttribute(List<Service> services, List<Relation> relations, UseCaseResponsibility useCaseResponsibility) {
        /** Write here the implementation of your quality attribute assessment */

        return Result.passed;
    }

    @Override
    public void setNextChain(CheckerChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public void runAssessment(System system) {
        system.CheckAttribute(this.getClass().getSimpleName(), calculateNewAttribute(system.getServices(), system.getRelations(), system.getUseCaseResponsibilities()));

        if (this.chain != null) {
            this.chain.runAssessment(system);
        }
    }
}
