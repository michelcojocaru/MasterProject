package com.thesis.validator.helpers;

import com.thesis.validator.model.Service;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NLPOperations {

    private static final double SIMILARITY_COEFFICIENT_THRESHOLD = 0.2;
    private static ILexicalDatabase db = new NictWordNet();
    private static RelatednessCalculator[] rcs = {
            /*new HirstStOnge(db), new LeacockChodorow(db),
            new Resnik(db), new JiangConrath(db), new Lin(db),new Path(db), new Lesk(db),*/  new WuPalmer(db)
    };

    public static double calculateSemanticSimilarity(String word1, String word2) {
        WS4JConfiguration.getInstance().setMFS(true);
        for (RelatednessCalculator rc : rcs) {
            return rc.calcRelatednessOfWords(word1, word2);
            //CrystalGlobe.out.println(rc.getClass().getName() + "\t" + s);
        }
        return 0.0;
    }

    public static double checkSemanticSimilarity(List<Service> services) {
        ArrayList<Double> similarities = new ArrayList<>();

        for (Service service : services) {
            HashSet<String> distinctEntities = Operations.getDistinctEntities(service);
            if (distinctEntities.size() > 1) {
                String[] entities = new String[distinctEntities.size()];
                distinctEntities.toArray(entities);
                for (int i = 0; i < entities.length; i++) {
                    for (int j = i + 1; j < entities.length; j++) {
                        similarities.add(calculateSemanticSimilarity(entities[i], entities[j]));
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
