package com.thesis.validator.logic;

import com.thesis.validator.enums.Mark;
import com.thesis.validator.model.CrystalGlobe;
import com.thesis.validator.model.TestResult;

public interface CheckerChain {

    void setNextChain(CheckerChain nextChain);

    void runAssessment(CrystalGlobe crystalGlobe);

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
