package com.thesis.validator.enums;

public enum Feedback {
        LOW_CAUSE_DEPENDENCIES("We detected an abnormally high variation in inward vs outward dependencies count per microservice!"),
        LOW_TREATMENT_DEPENDENCIES("We recommend revising the dependencies of the system."),
        MEDIUM_CAUSE_DEPENDENCIES("We detected an medium variation in inward vs outward dependencies count per microservice!"),
        MEDIUM_TREATMENT_DEPENDENCIES("We recommend revising the use of Bounded Contexts in the design process."),
        HIGH_CAUSE_DEPENDENCIES("We detected optimal dependencies between the microservices"),
        HIGH_TREATMENT_DEPENDENCIES("No need."),

        LOW_CAUSE_SCC("We detected an abnormally high variation in inward vs outward dependencies count per microservice!"),
        LOW_TREATMENT_SCC("We recommend revising the dependencies of the system."),
        MEDIUM_CAUSE_SCC("We detected an medium variation in inward vs outward dependencies count per microservice!"),
        MEDIUM_TREATMENT_SCC("We recommend revising the use of Bounded Contexts in the design process."),
        HIGH_CAUSE_SCC("We detected optimal dependencies between the microservices"),
        HIGH_TREATMENT_SCC("No need."),

        ;

        private final String feedback;

        Feedback(final String feedback) {
            this.feedback = feedback;
        }

        @Override
        public String toString() {
            return feedback;
        }
}
