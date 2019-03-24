package com.thesis.validator.logic;


public class Checker {

    CheckerChain[] chain = {
            new GranularityChecker(),
            new CohesionChecker(),
            new CouplingChecker(),
            //register new System
            new NewAttributeChecker(),
    };

    public Checker() {
        for (int i = 0; i < chain.length - 1; i++) {
            chain[i].setNextChain(chain[i + 1]);
        }
    }

    public CheckerChain getFirstTest() {
        return this.chain[0];
    }
}
