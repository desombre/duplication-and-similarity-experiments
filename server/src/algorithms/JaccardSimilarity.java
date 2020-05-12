package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class JaccardSimilarity implements StringSimilarityAlgorithm {


    @Override
    public double calculateSimilarity(String s1, String s2) {
        List<String> lemmatizedI1Description = getTokens(s1);


        List<String> lemmatizedI2Description = getTokens(s2);
        List<String> concatenatedList = new ArrayList<>();
        concatenatedList.addAll(lemmatizedI1Description);
        concatenatedList.addAll(lemmatizedI2Description);

        int unionCount = uniqueElements(concatenatedList).length;

        // Jaccard similarity: (|A| + |B| - |A u B|) / |A u B|
        return (uniqueElements(lemmatizedI1Description).length + uniqueElements(lemmatizedI2Description).length - unionCount) / (double) unionCount;
    }


    private List<String> getTokens(String s) {
        return Arrays.asList(s.toLowerCase().split(" "));
    }

    private String[] uniqueElements(List<String> list) {
        HashSet<String> hashedArray = new HashSet<String>(list);
        return hashedArray.toArray(String[]::new);
    }

}
