package com.thesis.validator.logic;


import java.util.ArrayList;
import java.util.List;

public class Chain {

    private List<Checker> chain;

    public Chain() {
        this.chain = new ArrayList<>();
        this.chain.add(new GranularityChecker());
        this.chain.add(new CouplingChecker());
        this.chain.add(new CohesionChecker());
        //chain.add(new NewChecker());

        setChainOrder();
    }

    public Checker getFirstChecker() {
        return this.chain.get(0);
    }

    public void registerAttributeChecker(Checker checker){
        this.chain.add(checker);
        setChainOrder();
    }

    private void setChainOrder(){
        for (int i = 0; i < chain.size() - 1; i++) {
            chain.get(i).setNextInChain(chain.get(i + 1));
        }
    }
}
