package algorithms;

import org.hibernate.validator.internal.util.stereotypes.Immutable;

public class LevenshteinDistance implements StringSimilarityAlgorithm {

    // adaption of https://github.com/tdebatty/java-string-similarity/blob/master/src/main/java/info/debatty/java/stringsimilarity/Levenshtein.java

    public double calculateSimilarity(String s1, String s2) {

        int[] v0 = new int[s2.length() + 1];
        int[] v1 = new int[s2.length() + 1];
        int[] vtemp;

        for (int i = 0; i < v0.length; i++) {
            v0[i] = i;
        }

        for (int i = 0; i < s1.length(); i++) {

            v1[0] = i + 1;

            int minv1 = v1[0];

            // use formula to fill in the rest of the row
            for (int j = 0; j < s2.length(); j++) {
                int cost = 1;
                if (s1.charAt(i) == s2.charAt(j)) {
                    cost = 0;
                }
                v1[j + 1] = Math.min(
                        v1[j] + 1,              // Cost of insertion
                        Math.min(
                                v0[j + 1] + 1,  // Cost of remove
                                v0[j] + cost)); // Cost of substitution

                minv1 = Math.min(minv1, v1[j + 1]);
            }

            vtemp = v0;
            v0 = v1;
            v1 = vtemp;

        }
        int maxStringLength = Math.max(s1.length(), s2.length());
        int minStringLength = Math.min(s1.length(), s2.length());

        return 1 - ((double) v0[s2.length()]) / ((double) maxStringLength);
    }
}


