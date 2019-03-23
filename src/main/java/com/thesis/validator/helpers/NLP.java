package com.thesis.validator.helpers;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

public class NLP {

    private static ILexicalDatabase db = new NictWordNet();
    private static RelatednessCalculator[] rcs = {
            /*new HirstStOnge(db), new LeacockChodorow(db),
            new Resnik(db), new JiangConrath(db), new Lin(db),new Path(db), new Lesk(db),*/  new WuPalmer(db)
    };

    public static double calculateSemanticSimilarity(String word1, String word2) {
        WS4JConfiguration.getInstance().setMFS(true);
        for (RelatednessCalculator rc : rcs) {
            double s = rc.calcRelatednessOfWords(word1, word2);
            return s;
            //System.out.println(rc.getClass().getName() + "\t" + s);
        }
        return 0.0;
    }
}
