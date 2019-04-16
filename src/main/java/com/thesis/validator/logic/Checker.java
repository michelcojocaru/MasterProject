package com.thesis.validator.logic;


import java.util.ArrayList;
import java.util.List;

public class Checker {

    private List<Attribute> chain;

    public Checker() {
        this.chain = new ArrayList<>();
        this.chain.add(new GranularityChecker());
        this.chain.add(new CouplingChecker());
        this.chain.add(new CohesionChecker());
        //chain.add(new NewAttributeChecker());

        setChainOrder();
    }

    public Attribute getFirstChecker() {
        return this.chain.get(0);
    }

    public void registerAttributeChecker(Attribute checker){
        this.chain.add(checker);
        setChainOrder();
    }

    private void setChainOrder(){
        for (int i = 0; i < chain.size() - 1; i++) {
            chain.get(i).setNextInChain(chain.get(i + 1));
        }
    }
}
