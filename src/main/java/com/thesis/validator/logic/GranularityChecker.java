package com.thesis.validator.logic;

import com.thesis.validator.config.ClocInstaller;
import com.thesis.validator.enums.Averages;
import com.thesis.validator.enums.Feedback;
import com.thesis.validator.enums.Tests;
import com.thesis.validator.file.ExternalProgramExecutor;
import com.thesis.validator.file.FileSearch;
import com.thesis.validator.file.GitRepoDownloader;
import com.thesis.validator.file.GitRepoDownloader;
import com.thesis.validator.helpers.MathOperations;
import com.thesis.validator.helpers.Operations;
import com.thesis.validator.model.CrystalGlobe;
import com.thesis.validator.model.Repo;
import com.thesis.validator.model.Service;
import com.thesis.validator.model.TestResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GranularityChecker implements CheckerChain {

    private static final double GRANULARITY_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.5;
    private CheckerChain chain;

    // calculate the coefficient of variation between the lengths
    // of nanoentity lists from each service
    private static HashMap<String,TestResult> calculateGranularity(List<Service> services, Averages averageType, Repo repo) {
        final int N = services.size();
        double[] serviceScores = new double[N];
        double[] locScores = new double[N];
        HashMap<String, TestResult> resultScores = new HashMap<>();
        TestResult testResult;
        HashSet<String> entities;
        double result;
        int i = 0;

        for (Service service : services) {
            entities = Operations.getEntities(service, false);
            serviceScores[i++] = entities.size();
        }

        MathOperations.normalize(serviceScores);

        result = Math.abs(MathOperations.getCoefficientOfVariation(serviceScores, averageType) - 1) * 10.0;
        testResult = new TestResult(Tests.NANOENTITIES_COMPOSITION_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
        CheckerChain.PopulateCauseAndTreatment(testResult,
                Feedback.LOW_CAUSE_NANOENTITIES_COMPOSITION.toString(),
                Feedback.LOW_TREATMENT_NANOENTITIES_COMPOSITION.toString(),
                Feedback.MEDIUM_CAUSE_NANOENTITIES_COMPOSITION.toString(),
                Feedback.MEDIUM_TREATMENT_NANOENTITIES_COMPOSITION.toString(),
                Feedback.HIGH_CAUSE_NANOENTITIES_COMPOSITION.toString(),
                Feedback.HIGH_TREATMENT_NANOENTITIES_COMPOSITION.toString());
        resultScores.put(Tests.NANOENTITIES_COMPOSITION_TEST.name(), testResult);

        //TODO prettify code
        //long startTime = System.currentTimeMillis();
        Path pathToBeDeleted = Paths.get(System.getProperty("user.dir")).resolve("repo");
        if(GitRepoDownloader.deleteDirectory(pathToBeDeleted.toFile())){
            System.out.println("There was a previous repo!");
        }
        if(repo!= null && !repo.url.equals("")) {
            if (GitRepoDownloader.download(repo.url)) {
                i = 0;
                int lineCount;
                String stats = null;
                FileSearch fileSearch = FileSearch.getInstance();
                for (Service service : services) {
                    lineCount = 0;
                    entities = Operations.getEntities(service, true);
                    for (String entity : entities) {
                        if(repo.languages.size() > 0) {
                            for (String language : repo.languages) {
                                List<String> files = fileSearch.searchDirectory(new File(System.getProperty("user.dir") + "/repo"), entity + language);
                                for (String file : files) {
                                    try {
                                        if(ClocInstaller.isMac()) {
                                            stats = ExternalProgramExecutor.exec("/usr/local/Cellar/cloc/1.80/bin/cloc", file, "--json");
                                        }else if(ClocInstaller.isUnix()){
                                            stats = ExternalProgramExecutor.exec("/usr/bin/cloc", file, "--json");
                                        }
                                        if (stats != null) {
                                            JSONObject clocResult = new JSONObject(stats);
                                            JSONObject java = clocResult.getJSONObject("Java");
                                            String loc = java.getString("code");
                                            lineCount += Integer.parseInt(loc);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                fileSearch.clearResults();
                            }
                        }
                    }
                    locScores[i++] = lineCount;
                }

                pathToBeDeleted = Paths.get(System.getProperty("user.dir")).resolve("repo");
                if (GitRepoDownloader.deleteDirectory(pathToBeDeleted.toFile())) {
                    System.out.println("Successfully deleted repo!");
                }

                MathOperations.normalize(locScores);

                result = Math.abs(MathOperations.getCoefficientOfVariation(locScores, averageType) - 1) * 10.0;
                testResult = new TestResult(Tests.LOC_TEST, Double.parseDouble(new DecimalFormat(".#").format(result)));
                CheckerChain.PopulateCauseAndTreatment(testResult,
                        Feedback.LOW_CAUSE_LOC.toString(),
                        Feedback.LOW_TREATMENT_LOC.toString(),
                        Feedback.MEDIUM_CAUSE_LOC.toString(),
                        Feedback.MEDIUM_TREATMENT_LOC.toString(),
                        Feedback.HIGH_CAUSE_LOC.toString(),
                        Feedback.HIGH_TREATMENT_LOC.toString());
                resultScores.put(Tests.LOC_TEST.name(), testResult);
            }
        }

        //long stopTime = System.currentTimeMillis();
        //long elapsedTime = stopTime - startTime;
        //System.out.println("LOC test time:" + elapsedTime);

        return resultScores;
    }


    @Override
    public void setNextChain(CheckerChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public void runAssessment(CrystalGlobe crystalGlobe) {
        crystalGlobe.CheckAttribute(this.getClass().getSimpleName(), calculateGranularity(crystalGlobe.getServices(), crystalGlobe.getTypeOfAverage(), crystalGlobe.getRepo()));

        if (this.chain != null) {
            this.chain.runAssessment(crystalGlobe);
        }
    }
}
