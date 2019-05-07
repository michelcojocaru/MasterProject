package com.thesis.validator.helpers;

import com.thesis.validator.enums.SimilarityAlgorithms;
import com.thesis.validator.model.Service;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.*;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class NLPOperations {

    private static final double SIMILARITY_COEFFICIENT_THRESHOLD = 0.2;
    private static ILexicalDatabase db = new NictWordNet();
    private static RelatednessCalculator calculator;
    private static HashMap<SimilarityAlgorithms,RelatednessCalculator> rcs;

    static {
        //fallback
        calculator = new WuPalmer(db);

        rcs = new HashMap<>();
        rcs.put(SimilarityAlgorithms.HIRST_ST_ONGE, new HirstStOnge(db));
        rcs.put(SimilarityAlgorithms.LEACOCK_CHODOROW, new LeacockChodorow(db));
        rcs.put(SimilarityAlgorithms.RESNIK, new Resnik(db));
        rcs.put(SimilarityAlgorithms.JIANG_CONRATH, new JiangConrath(db));
        rcs.put(SimilarityAlgorithms.LIN, new Lin(db));
        rcs.put(SimilarityAlgorithms.PATH, new Path(db));
        rcs.put(SimilarityAlgorithms.LESK, new Lesk(db));
        rcs.put(SimilarityAlgorithms.WU_PALMER, new WuPalmer(db));
    }

    public static double calculateSemanticSimilarity(String word1, String word2, SimilarityAlgorithms algorithm) {
        WS4JConfiguration.getInstance().setMFS(true);
        calculator = rcs.get(algorithm);

        return calculator.calcRelatednessOfWords(word1, word2);
    }

    public static String[] splitWords(String entity){
        return entity.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");

    }

    public static double checkSemanticSimilarity(List<Service> services, SimilarityAlgorithms algorithm) {
        ArrayList<Double> similarities = new ArrayList<>();

        for (Service service : services) {
            HashSet<String> distinctEntities = Operations.getEntities(service, true);
            if (distinctEntities.size() > 1) {
                String[] entities = new String[distinctEntities.size()];
                distinctEntities.toArray(entities);
                for (int i = 0; i < entities.length; i++) {
                    for (int j = i + 1; j < entities.length; j++) {
                        String[] entityOneItems = splitWords(entities[i]);
                        String[] entityTwoItems = splitWords(entities[j]);

                        if(entityOneItems.length > 1 || entityTwoItems.length > 1){
                            ArrayList<Double> innerSimilarities = new ArrayList<>();
                            for (String itemOne : entityOneItems) {
                                for (String itemTwo : entityTwoItems) {
                                    double sim = calculateSemanticSimilarity(itemOne, itemTwo, algorithm);
                                    innerSimilarities.add(sim);
                                }
                            }
                            similarities.add(calculateMeanOfSimilarities(innerSimilarities));
                        } else {
                            similarities.add(calculateSemanticSimilarity(entities[i], entities[j], algorithm));
                        }
                    }
                }
            }else if(distinctEntities.size() == 1){
                similarities.add(0.0);
            }
        }
        return scoreToMark(calculateMeanOfSimilarities(similarities));
    }

    private static double calculateMeanOfSimilarities(ArrayList<Double> similarities) {
        double median = MathOperations.median(Operations.ListToArray(similarities));

        if (median == 0.0) {
            median = MathOperations.average(Operations.ListToArray(similarities));
        }

        return median;
    }

    private static double scoreToMark(double median){
        return Math.abs(median - 1) * 10;
    }
}
