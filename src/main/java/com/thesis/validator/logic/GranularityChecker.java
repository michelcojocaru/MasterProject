package com.thesis.validator.logic;

import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.Feedback;
import com.thesis.validator.enums.SimilarityAlgorithms;
import com.thesis.validator.enums.Tests;
import com.thesis.validator.file.FileSearch;
import com.thesis.validator.file.GitRepoDownloader;
import com.thesis.validator.helpers.CSVOperations;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.helpers.Operations;
import com.thesis.validator.helpers.TextOperations;
import com.thesis.validator.model.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GranularityChecker extends Checker {

    private static final double GRANULARITY_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.5;
    private Checker chain;

    // calculate the coefficient of variation between the lengths
    // of nanoentity lists from each service
    public HashMap<String,TestResult> assessAttribute(String systemName,
                                                      List<Service> services,
                                                      List<Relation> relations,
                                                      UseCaseResponsibility useCaseResponsibilities,
                                                      Averages averageType,
                                                      List<SimilarityAlgorithms> algorithms,
                                                      Repo repo) {
        long startCheckerTime = System.nanoTime(), endCheckerTime;
        long startTestTime, endTestTime;
        long duration;
        final int N = services.size();
        double[] serviceScores = new double[N];
        double[] locScores = new double[N];
        HashMap<String, TestResult> resultScores = new HashMap<>();
        TestResult testResult;
        HashSet<String> entities;
        double result;
        int i = 0;

        startTestTime = System.nanoTime();
        testResult = new TestResult(Tests.NANOENTITIES_COMPOSITION_TEST);
        for (Service service : services) {
            entities = Operations.getEntities(service, false);
            serviceScores[i++] = entities.size();
            Checker.PopulateDetails(testResult, service.name, String.valueOf(entities.size()), "Service", "nanoentities");
        }

        MathOperations.normalize(serviceScores);

        result = Math.abs(MathOperations.getCoefficientOfVariation(serviceScores, averageType) - 1) * 10.0;
        testResult.setScore(Double.parseDouble(new DecimalFormat(".#").format(result)));
        Checker.PopulateCauseAndTreatment(testResult,
                Feedback.LOW_CAUSE_NANOENTITIES_COMPOSITION.toString(),
                Feedback.LOW_TREATMENT_NANOENTITIES_COMPOSITION.toString(),
                Feedback.MEDIUM_CAUSE_NANOENTITIES_COMPOSITION.toString(),
                Feedback.MEDIUM_TREATMENT_NANOENTITIES_COMPOSITION.toString(),
                Feedback.HIGH_CAUSE_NANOENTITIES_COMPOSITION.toString(),
                Feedback.HIGH_TREATMENT_NANOENTITIES_COMPOSITION.toString());
        resultScores.put(Tests.NANOENTITIES_COMPOSITION_TEST.name(), testResult);

        endTestTime = System.nanoTime();
        duration = (endTestTime - startTestTime);
        System.out.print(Tests.NANOENTITIES_COMPOSITION_TEST.toString() + "," + duration + ",");
        CSVOperations.logRunTimes(systemName, new String[]{ Tests.NANOENTITIES_COMPOSITION_TEST.toString() , String.valueOf(duration)});
        //System.out.println("NANOENTITIES_COMPOSITION_TEST took: " + duration + " nanoseconds.");



        //TODO prettify code
        Path pathToBeDeleted = Paths.get(System.getProperty("user.dir")).resolve("repo");
        if(GitRepoDownloader.deleteDirectory(pathToBeDeleted.toFile())){
            System.out.println("There was a previous repo!");
        }
        if(repo!= null && !repo.url.equals("")) {
            if (GitRepoDownloader.download(repo.url)) {
                startTestTime = System.nanoTime();
                testResult = new TestResult(Tests.LOC_TEST);
                i = 0;
                int lineCount;
                FileSearch fileSearch = FileSearch.getInstance();
                for (Service service : services) {
                    lineCount = 0;
                    entities = Operations.getEntities(service, true);
                    for (String entity : entities) {
                        if(repo.languages.size() > 0) {
                            for (String language : repo.languages) {
                                List<String> files = fileSearch.searchDirectory(new File(System.getProperty("user.dir") + "/repo"), entity + language);
                                for (String file : files) {
                                    lineCount += TextOperations.countLines(file);
                                }
                                fileSearch.clearResults();
                            }
                        }
                    }
                    locScores[i++] = lineCount;
                    Checker.PopulateDetails(testResult, service.name, String.valueOf(lineCount), "Microservice", "lines of code");
                }

                pathToBeDeleted = Paths.get(System.getProperty("user.dir")).resolve("repo");
                if (GitRepoDownloader.deleteDirectory(pathToBeDeleted.toFile())) {
                    System.out.println("Successfully deleted repo!");
                }

                MathOperations.normalize(locScores);

                result = Math.abs(MathOperations.getCoefficientOfVariation(locScores, averageType) - 1) * 10.0;
                testResult.setScore(Double.parseDouble(new DecimalFormat(".#").format(result)));
                Checker.PopulateCauseAndTreatment(testResult,
                        Feedback.LOW_CAUSE_LOC.toString(),
                        Feedback.LOW_TREATMENT_LOC.toString(),
                        Feedback.MEDIUM_CAUSE_LOC.toString(),
                        Feedback.MEDIUM_TREATMENT_LOC.toString(),
                        Feedback.HIGH_CAUSE_LOC.toString(),
                        Feedback.HIGH_TREATMENT_LOC.toString());
                resultScores.put(Tests.LOC_TEST.name(), testResult);
            }
        }
        endTestTime = System.nanoTime();
        duration = (endTestTime - startTestTime);
        System.out.print(Tests.LOC_TEST.toString() + "," + duration + ",");
        CSVOperations.logRunTimes(systemName, new String[]{ Tests.LOC_TEST.toString() , String.valueOf(duration)});
        //System.out.println("LOC_TEST took: " + duration + " nanoseconds.");

        endCheckerTime = System.nanoTime();
        duration = (endCheckerTime - startCheckerTime);  //divide by 1000000 to get milliseconds.
        //System.out.println("GranularityChecker took: " + duration + " nanoseconds.");
        return resultScores;
    }
}
