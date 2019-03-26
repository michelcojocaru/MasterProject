package com.thesis.validator.logic;

import com.thesis.validator.model.CrystalGlobe;

public interface CheckerChain {

    void setNextChain(CheckerChain nextChain);

    void runAssessment(CrystalGlobe crystalGlobe);
}
