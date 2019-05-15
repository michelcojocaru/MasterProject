# MasterProject

**Custom JSON input format:**
```json
{
  "services": [
    {
      "nanoentities": ["String"],
      "id": "String",
      "name": "String"
    }
  ],
  "relations": [
    {
      "serviceA": "String",
      "serviceB": "String",
      "sharedEntities": ["String"],
      "direction": "INCOMING/OUTGOING"
    }
  ],
  "useCaseResponsibility": {
    "Service A": ["String"]
  },
  "repo": {
    "url": "String",
    "languages": [
      ".java"
    ]
  }
}
```
**Export result format:**
```json
{
	"NewChecker": {
		"NEWATTRIBUTE_TEST": {
			"score": 0,
			"cause": "Fill in the cause of the test fail.",
			"treatment": "Fill in the recommendation for fixing the fail.",
			"details": null,
			"testName": "NEWATTRIBUTE_TEST"
		}
	},
	"CouplingChecker": {
		"DEPENDENCIES_COMPOSITION_TEST": {
			"score": 10.0,
			"cause": "String",
			"treatment": "String",
			"details": "String",
			"testName": "DEPENDENCIES_COMPOSITION_TEST"
		},
		"SCC_TEST": {
			"score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
			"testName": "SCC_TEST"
		}
	},
	"GranularityChecker": {
		"NANOENTITIES_COMPOSITION_TEST": {
			"score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
            "testName": "NANOENTITIES_COMPOSITION_TEST"
		},
		"LOC_TEST": {
			"score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
            "testName": "LOC_TEST"
		}
	},
	"CohesionChecker": {
		"ENTITIES_COMPOSITION_TEST": {
            "score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
            "testName": "ENTITIES_COMPOSITION_TEST"
		},
		"RESNIK": {
			"score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
            "testName": "SEMANTIC_SIMILARITY_TEST"
		},
		"LIN": {
			"score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
            "testName": "SEMANTIC_SIMILARITY_TEST"
		},
		"PATH": {
			"score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
            "testName": "SEMANTIC_SIMILARITY_TEST"
		},
		"WU_PALMER": {
			"score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
            "testName": "SEMANTIC_SIMILARITY_TEST"
		},
		"LESK": {
			"score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
            "testName": "SEMANTIC_SIMILARITY_TEST"
		},
		"JIANG_CONRATH": {
			"score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
            "testName": "SEMANTIC_SIMILARITY_TEST"
		},
		"RESPONSIBILITIES_COMPOSITION_TEST": {
            "score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
            "testName": "RESPONSIBILITIES_COMPOSITION_TEST"
		},
		"RELATIONS_COMPOSITION_TEST": {
			"score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
            "testName": "RELATIONS_COMPOSITION_TEST"
		},
		"LEACOCK_CHODOROW": {
            "score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
            "testName": "SEMANTIC_SIMILARITY_TEST"
		},
		"HIRST_ST_ONGE": {
			"score": 10.0,
            "cause": "String",
            "treatment": "String",
            "details": "String",
            "testName": "SEMANTIC_SIMILARITY_TEST"
		}
	}
}
```
