package algorithms;

import data.DuplicateTextFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DuplicationTextUtil {
    private static final int minDuplicateLength = 3;

    public static List<DuplicateTextFragment> detectDuplicateTextFragments(String s1, String s2) {
        List<DuplicateTextFragment> duplicateList = new ArrayList();
        List<String> preprocessedS1Tokens = preprocess(s1);

        // iterate over string s1 as list
        int index = 0;
        while (index < preprocessedS1Tokens.size() - minDuplicateLength + 1) {
            int numberOfDuplicateTokens = minDuplicateLength;
            int indexOfDuplicate = 0;
            int lastIndexOfDuplicate = 0;

            boolean duplicateFound = false;
            String stringToSearch = "";
            String lastStringToSearch = "";

            // build DuplicateTextFragment in loop?
            while (indexOfDuplicate != -1) {
                lastIndexOfDuplicate = indexOfDuplicate;
                lastStringToSearch = stringToSearch;
                if (index + numberOfDuplicateTokens > preprocessedS1Tokens.size()) {
                    break;
                }
                stringToSearch = generateStringToSearch(preprocessedS1Tokens, index, numberOfDuplicateTokens);
                indexOfDuplicate = s2.indexOf(stringToSearch);
                if (!duplicateFound) {
                    duplicateFound = indexOfDuplicate != -1;
                }
                numberOfDuplicateTokens++;
            }
            if (duplicateFound) {
                duplicateList.add(new DuplicateTextFragment(s1, s2, lastIndexOfDuplicate, lastStringToSearch.length()));
                index += numberOfDuplicateTokens -2; // number of duplicate tokens is one more than found tokens
            }

            index++;

        }
        return duplicateList;
    }

    private static String generateStringToSearch(List<String> preprocessedS1Tokens, int index, int numberOfDuplicateTokens) {
        StringBuilder stringToSearch = new StringBuilder();
        for (int i = index; i < preprocessedS1Tokens.size() && i < index + numberOfDuplicateTokens; i++) {
            stringToSearch.append(preprocessedS1Tokens.get(i)).append(" ");
        }
        return stringToSearch.toString().trim();
    }

    private static List<String> preprocess(String s1) {
        return Arrays.asList(s1.split(" "));
    }

}
