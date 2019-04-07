package com.thesis.validator.model;

import com.thesis.validator.enums.Tests;

public class TestResult {

    private Tests name;
    private double score;
    private String cause;
    private String treatment;

    public Tests getTestName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public String getCause() {
        return cause;
    }

    public String getTreatment() {
        return treatment;
    }


    public TestResult(Tests name, double score){
        this.name = name;
        this.score = score;

//        //TODO give real cause & treatment
//        if(name.equals(Tests.NANOENTITIES_COMPOSITION_TEST)){
//            if(score > 0 && score < 5){
//                this.setCause("We detected an abnormally high variation in microservice's sizes!");
//                this.setTreatment("We recommend revising the use of Bounded Contexts in the design process.");
//            }else if(score >= 5 && score < 7.5){
//                this.setCause("We detected an medium variation in microservice's sizes!");
//                this.setTreatment("We recommend revising the use of Bounded Contexts in the design process.");
//            }else if(score >= 7.5 && score <= 10){
//                this.setCause("We detected optimal microservice's sizes!");
//                this.setTreatment("No need.");
//            }
//        }else if(name.equals(Tests.COUPLING_COMPOSITION_TEST)) {
//            if (score > 0 && score < 5) {
//                this.setCause("We detected an abnormally high variation in inward vs outward dependencies count per microservice!");
//                this.setTreatment("We recommend revising the dependencies of the system.");
//            } else if (score >= 5 && score < 7.5) {
//                this.setCause("We detected an medium variation in inward vs outward dependencies count per microservice!");
//                this.setTreatment("We recommend revising the use of Bounded Contexts in the design process.");
//            } else if (score >= 7.5 && score <= 10) {
//                this.setCause("We detected optimal dependencies between the microservices");
//                this.setTreatment("No need.");
//            }
//        }else if(name.equals(Tests.ENTITIES_COMPOSITION_TEST)) {
//            if (score > 0 && score < 5) {
//                this.setCause("We detected an abnormally high variation in entities counts between microservices");
//                this.setTreatment("We recommend revising the use of Bounded Contexts in the design process.");
//            } else if (score >= 5 && score < 7.5) {
//                this.setCause("We detected an medium variation in inward vs outward dependencies count per microservice!");
//                this.setTreatment("We recommend revising the use of Bounded Contexts in the design process.");
//            } else if (score >= 7.5 && score <= 10) {
//                this.setCause("We detected optimal dependencies between the microservices");
//                this.setTreatment("No need.");
//            }
//        }else if(name.equals(Tests.RELATIONS_COMPOSITION_TEST)) {
//            if (score > 0 && score < 5) {
//                this.setCause("We detected an abnormally high variation in inward vs outward dependencies count per microservice!");
//                this.setTreatment("We recommend revising the dependencies of the system.");
//            } else if (score >= 5 && score < 7.5) {
//                this.setCause("We detected an medium variation in inward vs outward dependencies count per microservice!");
//                this.setTreatment("We recommend revising the use of Bounded Contexts in the design process.");
//            } else if (score >= 7.5 && score <= 10) {
//                this.setCause("We detected optimal dependencies between the microservices");
//                this.setTreatment("No need.");
//            }
//        }else if(name.equals(Tests.RESPONSIBILITIES_COMPOSITION_TEST)) {
//            if (score > 0 && score < 5) {
//                this.setCause("We detected an abnormally high variation in inward vs outward dependencies count per microservice!");
//                this.setTreatment("We recommend revising the dependencies of the system.");
//            } else if (score >= 5 && score < 7.5) {
//                this.setCause("We detected an medium variation in inward vs outward dependencies count per microservice!");
//                this.setTreatment("We recommend revising the use of Bounded Contexts in the design process.");
//            } else if (score >= 7.5 && score <= 10) {
//                this.setCause("We detected optimal dependencies between the microservices");
//                this.setTreatment("No need.");
//            }
//        }else if(name.equals(Tests.SEMANTIC_SIMILARITY_TEST)) {
//            if (score > 0 && score < 5) {
//                this.setCause("We detected an abnormally high variation in inward vs outward dependencies count per microservice!");
//                this.setTreatment("We recommend revising the dependencies of the system.");
//            } else if (score >= 5 && score < 7.5) {
//                this.setCause("We detected an medium variation in inward vs outward dependencies count per microservice!");
//                this.setTreatment("We recommend revising the use of Bounded Contexts in the design process.");
//            } else if (score >= 7.5 && score <= 10) {
//                this.setCause("We detected optimal dependencies between the microservices");
//                this.setTreatment("No need.");
//            }
//        }else if(name.equals(Tests.NEWATTRIBUTE_TEST)) {
//            if (score > 0 && score < 5) {
//                this.setCause("We detected an abnormally high variation in inward vs outward dependencies count per microservice!");
//                this.setTreatment("We recommend revising the dependencies of the system.");
//            } else if (score >= 5 && score < 7.5) {
//                this.setCause("We detected an medium variation in inward vs outward dependencies count per microservice!");
//                this.setTreatment("We recommend revising the use of Bounded Contexts in the design process.");
//            } else if (score >= 7.5 && score <= 10) {
//                this.setCause("We detected optimal dependencies between the microservices");
//                this.setTreatment("No need.");
//            }
//        }
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
    public void setName(Tests name) {
        this.name = name;
    }
    public void setScore(double score) {
        this.score = score;
    }
}
