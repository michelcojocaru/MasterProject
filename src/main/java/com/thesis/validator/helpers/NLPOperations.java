package com.thesis.validator.helpers;

import com.thesis.validator.enums.SimilarityAlgos;
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
    private static HashMap<SimilarityAlgos,RelatednessCalculator> rcs;

    static {
        //fallback
        calculator = new WuPalmer(db);

        rcs = new HashMap<>();
        rcs.put(SimilarityAlgos.HIRST_ST_ONGE, new HirstStOnge(db));
        rcs.put(SimilarityAlgos.LEACOCK_CHODOROW, new LeacockChodorow(db));
        rcs.put(SimilarityAlgos.RESNIK, new Resnik(db));
        rcs.put(SimilarityAlgos.JIANG_CONRATH, new JiangConrath(db));
        rcs.put(SimilarityAlgos.LIN, new Lin(db));
        rcs.put(SimilarityAlgos.PATH, new Path(db));
        rcs.put(SimilarityAlgos.LESK, new Lesk(db));
        rcs.put(SimilarityAlgos.WU_PALMER, new WuPalmer(db));
    }

    public static double calculateSemanticSimilarity(String word1, String word2, SimilarityAlgos algorithm) {
        WS4JConfiguration.getInstance().setMFS(true);
        calculator = rcs.get(algorithm);

        return calculator.calcRelatednessOfWords(word1, word2);
    }

    public static double checkSemanticSimilarity(List<Service> services, SimilarityAlgos algorithm) {
        ArrayList<Double> similarities = new ArrayList<>();

        for (Service service : services) {
            HashSet<String> distinctEntities = Operations.getDistinctEntities(service);
            if (distinctEntities.size() > 1) {
                String[] entities = new String[distinctEntities.size()];
                distinctEntities.toArray(entities);
                for (int i = 0; i < entities.length; i++) {
                    for (int j = i + 1; j < entities.length; j++) {
                        similarities.add(calculateSemanticSimilarity(entities[i], entities[j], algorithm));
                    }
                }
            }
        }

        double median = MathOperations.median(Operations.ListToArray(similarities));

        if (median == 0.0) {
            median = MathOperations.average(Operations.ListToArray(similarities));
        }

        return Math.abs(median - 1) * 10;
    }
}
