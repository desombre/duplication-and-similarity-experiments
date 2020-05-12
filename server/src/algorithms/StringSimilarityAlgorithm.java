package algorithms;

@FunctionalInterface
public interface StringSimilarityAlgorithm {
    /**
     * Compares to strings and returns the similarity.
     *
     * @param s1: String one to campare.
     * @param s2: String two to compare.
     * @return similarity value in [0, 1]
     */
    double calculateSimilarity(String s1, String s2);

}
