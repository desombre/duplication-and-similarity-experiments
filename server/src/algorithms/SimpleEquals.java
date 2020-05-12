package algorithms;

public class SimpleEquals implements StringSimilarityAlgorithm {
    @Override
    public double calculateSimilarity(String s1, String s2) {
        return s1.equals(s2) ? 1 : 0;
    }
}
