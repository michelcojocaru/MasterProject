package com.thesis.validator.logic;

import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.Mark;
import com.thesis.validator.enums.SimilarityAlgorithms;
import com.thesis.validator.model.*;

import java.util.HashMap;
import java.util.List;

public abstract class Checker {

    private Checker chain;

    final void setNextInChain(Checker nextChain) {
        this.chain = nextChain;
    }

    final public void runAssessment(CrystalGlobe crystalGlobe) {
        crystalGlobe.CheckAttribute(this.getClass().getSimpleName(), assessAttribute(crystalGlobe.getServices(),
                                                                            crystalGlobe.getRelations(),
                                                                            crystalGlobe.getUseCaseResponsibilities(),
                                                                            crystalGlobe.getTypeOfAverage(),
                                                                            crystalGlobe.getSimilarityAlgorithm(),
                                                                            crystalGlobe.getRepo()));

        if (this.chain != null) {
            this.chain.runAssessment(crystalGlobe);
        }
    }

    abstract HashMap<String, TestResult> assessAttribute(List<Service> services,
                                                       List<Relation> relations,
                                                       UseCaseResponsibility useCaseResponsibilities,
                                                       Averages averageType,
                                                       List<SimilarityAlgorithms> algorithms,
                                                       Repo repo);

    // output helper messages for the user of the framework in regards
    // to what went wrong with a test and what could be done
    static void PopulateCauseAndTreatment(TestResult result,
                                                  String lowCause,
                                                  String lowTreatment,
                                                  String midCause,
                                                  String midTreatment,
                                                  String highCause,
                                                  String highTreatment){
        if(result.getScore() >= Mark.ZERO.getValue() && result.getScore() < Mark.LOW.getValue()){
            result.setCause(lowCause);
            result.setTreatment(lowTreatment);
        }else if(result.getScore() >= Mark.LOW.getValue() && result.getScore() < Mark.MID.getValue()){
            result.setCause(midCause);
            result.setTreatment(midTreatment);
        }else if(result.getScore() >= Mark.MID.getValue() && result.getScore() <= Mark.HIGH.getValue()){
            result.setCause(highCause);
            result.setTreatment(highTreatment);
        }
    }

}
