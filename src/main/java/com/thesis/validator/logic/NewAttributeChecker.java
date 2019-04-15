package com.thesis.validator.logic;

import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.Feedback;
import com.thesis.validator.enums.Tests;
import com.thesis.validator.model.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class NewAttributeChecker implements CheckerChain {

    private static final double NEW_ATTRIBUTE_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.0;
    private CheckerChain chain;

    private static HashMap<String, TestResult> calculateNewAttribute(List<Service> services, List<Relation> relations, UseCaseResponsibility useCaseResponsibility, Averages averageType) {
        HashMap<String,TestResult> resultScores = new HashMap<>();
        TestResult testResult = null;
        double result = 0.0;

        /** Write here
         * the implementation
         * of your quality attribute
         * assessment */

        testResult = new TestResult(Tests.NEWATTRIBUTE_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
        CheckerChain.PopulateCauseAndTreatment(testResult,
                Feedback.LOW_CAUSE_NEWATTRIBUTE.toString(),
                Feedback.LOW_TREATMENT_NEWATTRIBUTE.toString(),
                Feedback.MEDIUM_CAUSE_NEWATTRIBUTE.toString(),
                Feedback.MEDIUM_TREATMENT_NEWATTRIBUTE.toString(),
                Feedback.HIGH_CAUSE_NEWATTRIBUTE.toString(),
                Feedback.HIGH_TREATMENT_NEWATTRIBUTE.toString());
        resultScores.put(testResult.getTestName().name(),testResult);
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
