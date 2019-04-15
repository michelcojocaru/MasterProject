package com.thesis.validator.enums;

public enum Feedback {

        // relativity of sizes between microservices (GRANULARITY)
        LOW_CAUSE_NANOENTITIES_COMPOSITION("We detected high variation between microservice's sizes."),
        LOW_TREATMENT_NANOENTITIES_COMPOSITION("We recommend reconsidering the sizes of the microservices."),
        MEDIUM_CAUSE_NANOENTITIES_COMPOSITION("We detected an acceptable variation between microservice's sizes."),
        MEDIUM_TREATMENT_NANOENTITIES_COMPOSITION("We recommend reconsidering the sizes of the microservices."),
        HIGH_CAUSE_NANOENTITIES_COMPOSITION("We detected optimal variation between microservice's sizes."),
        HIGH_TREATMENT_NANOENTITIES_COMPOSITION("Nothing."),

        // relativity of sizes between microservices (GRANULARITY)
        LOW_CAUSE_LOC("We detected high variation between microservice's sizes (in LOC)."),
        LOW_TREATMENT_LOC("We recommend reconsidering the sizes of the microservices (in LOC)."),
        MEDIUM_CAUSE_LOC("We detected an acceptable variation between microservice's sizes (in LOC)."),
        MEDIUM_TREATMENT_LOC("We recommend reconsidering the sizes of the microservices (in LOC)."),
        HIGH_CAUSE_LOC("We detected optimal variation between microservice's sizes (in LOC)."),
        HIGH_TREATMENT_LOC("Nothing."),

        // variation between inward vs outward dependencies per microservice (COUPLING)
        LOW_CAUSE_DEPENDENCIES("We detected an abnormally high variation between inward vs outward dependencies count per microservice!"),
        LOW_TREATMENT_DEPENDENCIES("We recommend revising the dependencies between components."),
        MEDIUM_CAUSE_DEPENDENCIES("We detected an acceptable variation between inward vs outward dependencies count per microservice!"),
        MEDIUM_TREATMENT_DEPENDENCIES("We recommend revising the use of Bounded Contexts in the design process."),
        HIGH_CAUSE_DEPENDENCIES("We detected optimal dependencies between the microservices"),
        HIGH_TREATMENT_DEPENDENCIES("Nothing."),

        // variation between the entities counts per microservice (COHESION)
        LOW_CAUSE_ENTITIES_COMPOSITION("We detected an abnormally high variation between entities counts per microservice."),
        LOW_TREATMENT_ENTITIES_COMPOSITION("We recommend reconsidering the sizes of the microservices."),
        MEDIUM_CAUSE_ENTITIES_COMPOSITION("We detected an acceptable variation between entities counts per microservice."),
        MEDIUM_TREATMENT_ENTITIES_COMPOSITION("We recommend revising the use of Bounded Contexts in the design process."),
        HIGH_CAUSE_ENTITIES_COMPOSITION("We detected optimal variation between entities counts per microservice."),
        HIGH_TREATMENT_ENTITIES_COMPOSITION("Nothing."),

        // duplicate check for entities belonging to different microservices (COHESION)
        LOW_CAUSE_ENTITIES_DUPLICATES("We detected an abundance of duplicates among the entities of separate microservices."),
        LOW_TREATMENT_ENTITIES_DUPLICATES("We recommend revising the composition of the microservices."),
        MEDIUM_CAUSE_ENTITIES_DUPLICATES("We detected some duplicates among the entities of separate microservices."),
        MEDIUM_TREATMENT_ENTITIES_DUPLICATES("We recommend revising the composition of the microservices."),
        HIGH_CAUSE_ENTITIES_DUPLICATES("We detected no duplicates among the entities of separate microservices."),
        HIGH_TREATMENT_ENTITIES_DUPLICATES("Nothing."),

        // variation between the shared entities per relation (COHESION)
        LOW_CAUSE_RELATIONS_COMPOSITION("We detected an abnormally high variation between the shared entities counts per relation."),
        LOW_TREATMENT_RELATIONS_COMPOSITION("We recommend revising the shared concepts between the microservices."),
        MEDIUM_CAUSE_RELATIONS_COMPOSITION("We detected an acceptable variation between the shared entities counts per relation."),
        MEDIUM_TREATMENT_RELATIONS_COMPOSITION("We recommend revising the shared concepts between the microservices."),
        HIGH_CAUSE_RELATIONS_COMPOSITION("We detected optimal dependencies between the microservices"),
        HIGH_TREATMENT_RELATIONS_COMPOSITION("Nothing."),

        // duplicate check for shared entities belonging to different relations (COHESION)
        LOW_CAUSE_RELATIONS_DUPLICATES("We detected an abundance of duplicates among the entities of different relations."),
        LOW_TREATMENT_RELATIONS_DUPLICATES("We recommend revising the relations between the microservices."),
        MEDIUM_CAUSE_RELATIONS_DUPLICATES("We detected an acceptable number of duplicates among the entities of different relations."),
        MEDIUM_TREATMENT_RELATIONS_DUPLICATES("We recommend revising the relations between the microservices."),
        HIGH_CAUSE_RELATIONS_DUPLICATES("We detected no duplicates among the entities of different relations."),
        HIGH_TREATMENT_RELATIONS_DUPLICATES("Nothing."),

        // duplicate check for use cases belonging to different microservices (COHESION)
        LOW_CAUSE_RESPONSIBILITIES_COMPOSITION("We detected an abnormally high variation between the use-cases counts per microservice."),
        LOW_TREATMENT_RESPONSIBILITIES_COMPOSITION("We recommend revising the use cases."),
        MEDIUM_CAUSE_RESPONSIBILITIES_COMPOSITION("We detected an acceptable variation between the use-cases counts per microservice."),
        MEDIUM_TREATMENT_RESPONSIBILITIES_COMPOSITION("We recommend revising the use cases."),
        HIGH_CAUSE_RESPONSIBILITIES_COMPOSITION("We detected an optimal variation between the use-cases counts per microservice."),
        HIGH_TREATMENT_RESPONSIBILITIES_COMPOSITION("Nothing."),

        // variation between the use cases counts per microservice (COHESION)
        LOW_CAUSE_RESPONSIBILITIES_DUPLICATES("We detected an abundance of duplicates among the use cases of separate microservices."),
        LOW_TREATMENT_RESPONSIBILITIES_DUPLICATES("We recommend revising the use cases."),
        MEDIUM_CAUSE_RESPONSIBILITIES_DUPLICATES("We detected an acceptable number of duplicates among the entities of separate microservices."),
        MEDIUM_TREATMENT_RESPONSIBILITIES_DUPLICATES("We recommend revising the use cases."),
        HIGH_CAUSE_RESPONSIBILITIES_DUPLICATES("We detected no duplicates among the entities of separate microservices."),
        HIGH_TREATMENT_RESPONSIBILITIES_DUPLICATES("Nothing."),

        // strongly connected components (COHESION)
        LOW_CAUSE_SCC("We detected strongly connected components between the microservices."),
        LOW_TREATMENT_SCC("We recommend re-aggregating the microservices."),
        MEDIUM_CAUSE_SCC("We detected strongly connected components between the microservices."),
        MEDIUM_TREATMENT_SCC("We recommend re-aggregating the microservices."),
        HIGH_CAUSE_SCC("We identified no strongly connected components between the microservices."),
        HIGH_TREATMENT_SCC("Nothing."),

        // semantic similarity between the entities per microservice (COHESION)
        LOW_CAUSE_SEMANTIC_SIMILARITY("We detected a poor semantic similarity between the entities per microservice."),
        LOW_TREATMENT_SEMANTIC_SIMILARITY("We recommend renaming the entities."),
        MEDIUM_CAUSE_SEMANTIC_SIMILARITY("We detected an acceptable semantic similarity between the entities per microservice."),
        MEDIUM_TREATMENT_SEMANTIC_SIMILARITY("We recommend renaming the entities."),
        HIGH_CAUSE_SEMANTIC_SIMILARITY("We detected optimal semantic similarity between the entities per microservice."),
        HIGH_TREATMENT_SEMANTIC_SIMILARITY("Nothing."),

        // new attribute first test (NEW_ATTRIBUTE)
        LOW_CAUSE_NEWATTRIBUTE("Fill in the cause of the test fail."),
        LOW_TREATMENT_NEWATTRIBUTE("Fill in the recommendation for fixing the fail."),
        MEDIUM_CAUSE_NEWATTRIBUTE("Fill in the cause of the test poor score."),
        MEDIUM_TREATMENT_NEWATTRIBUTE("Fill in the recommendation for growing the mark."),
        HIGH_CAUSE_NEWATTRIBUTE("Fill in the cause of the test high score."),
        HIGH_TREATMENT_NEWATTRIBUTE("Usually nothing."),

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
