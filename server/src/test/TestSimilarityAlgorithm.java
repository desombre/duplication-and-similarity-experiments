package test;

import algorithms.StringSimilarityAlgorithm;

import static org.junit.Assert.assertTrue;

public class TestSimilarityAlgorithm {
    private static final double SIMILARITY_THRESHOLD = 0.9;
    private String stringOne = "string1";
    private String stringDifferentToStringOne = "blubb";
    private String stringEqualToStringOne = "string1";
    private String stringSimilarToStringOne = "stringg1";


    protected void testAlgorithm(StringSimilarityAlgorithm algorithm) {
        this.testEqualStrings(algorithm);
        this.testDifferentStrings(algorithm);

    }

    private void testEqualStrings(StringSimilarityAlgorithm algorithm){
        assertTrue(algorithm.calculateSimilarity(stringOne, stringEqualToStringOne) > SIMILARITY_THRESHOLD);
    }

    private void testDifferentStrings(StringSimilarityAlgorithm algorithm){
        assertTrue(algorithm.calculateSimilarity(stringOne, stringDifferentToStringOne) < SIMILARITY_THRESHOLD);
    }
}
