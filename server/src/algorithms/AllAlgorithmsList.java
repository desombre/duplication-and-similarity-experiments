package algorithms;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AllAlgorithmsList {

    private static Map<String, StringSimilarityAlgorithm> availableAlgorithms = new HashMap<String, StringSimilarityAlgorithm>();

    static {
        availableAlgorithms.put(SimpleEquals.class.getSimpleName(), new SimpleEquals());
        availableAlgorithms.put(LevenshteinDistance.class.getSimpleName(), new LevenshteinDistance());
        availableAlgorithms.put(JaccardSimilarity.class.getSimpleName(), new JaccardSimilarity());
    }

    public static Optional<StringSimilarityAlgorithm> getAlgorithm(String key) {
        return Optional.ofNullable(availableAlgorithms.get(key));
    }

    public static List<String> getList() {
        return convertToList(availableAlgorithms.keySet());
    }

    public static <T> List<T> convertToList(Set<T> set) {
        List<T> items = new ArrayList<>();
        for (T e : set)
            items.add(e);
        return items;
    }
}
