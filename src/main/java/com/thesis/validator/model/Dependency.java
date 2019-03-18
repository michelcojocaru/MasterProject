package com.thesis.validator.model;

public class Dependency {

    private String serviceName;
    private int inward;
    private int outward;

    public Dependency(String serviceName, int inward, int to){
        this.serviceName = serviceName;
        this.inward = inward;
        this.outward = to;
    }

    public int getInward() {
        return inward;
    }

    public void setInward(int inward) {
        this.inward = inward;
    }

    public void incrementInward(){
        this.inward++;
    }

    public int getOutward() {
        return outward;
    }

    public void setOutward(int outward) {
        this.outward = outward;
    }

    public void incrementOutward(){
        this.outward++;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String name) {
        this.serviceName = name;
    }
}
