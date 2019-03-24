package com.thesis.validator.logic;

public interface CheckerChain {

    void setNextChain(CheckerChain nextChain);

    void runAssessment(System system);
}
