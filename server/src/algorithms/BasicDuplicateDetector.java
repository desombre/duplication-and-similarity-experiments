package algorithms;

import data.DuplicateTextFragment;

import javax.management.InvalidAttributeValueException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BasicDuplicateDetector {
    private static Preprocessor preprocessor;
    private static final String fieldUsedForDetection = "DESCRIPTION";
    private static final double MIN_SIMILARITY = 0.75;
    private int bandLength;

    public int getMinDuplicateLength() {
        return minDuplicateLength;
    }

    public void setMinDuplicateLength(int minDuplicateLength) {
        this.minDuplicateLength = minDuplicateLength;
    }

    private int minDuplicateLength;

    public BasicDuplicateDetector(int minDuplicateLength) {
        if (preprocessor == null) {
            preprocessor = new Preprocessor();

        }
        this.minDuplicateLength = minDuplicateLength;
        this.bandLength = (int) Math.floor(minDuplicateLength / MIN_SIMILARITY);
    }

    public BasicDuplicateDetector() {
        this(5);
    }

    private static String cleanMarkdown(String markdown) {
        return markdown.replaceAll("[\\|\\[\\]]+", " ").replaceAll("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", " ").replaceAll("h[0-9]+", "").replaceAll("[;\\/:*?\"<>&.{},'#!+@-]+", " ").replaceAll("[\n\r]+", " ").replaceAll("[0-9]+", " ").replaceAll("[\\s]+", " ");
    }

    public List<DuplicateTextFragment> detectDuplicateTextFragments(String s1, String s2) throws Exception {
        List<DuplicateTextFragment> duplicateList = new ArrayList();

        if (s1 != null && s2 != null) {
            s1 = cleanMarkdown(s1);
            s2 = cleanMarkdown(s2);

            BasicDuplicateDetector.preprocessor.preprocess(BasicDuplicateDetector.preprocessor.removeStopWords(s1));
            List<CharSequence> preprocessedS1Tokens = BasicDuplicateDetector.preprocessor.getTokens();

            BasicDuplicateDetector.preprocessor.preprocess(BasicDuplicateDetector.preprocessor.removeStopWords(s2));
            List<CharSequence> preprocessedS2Tokens = BasicDuplicateDetector.preprocessor.getTokens();

            //s2 = String.join(" ", preprocessedS2Tokens);

            duplicateList = detectDuplicateNew(preprocessedS1Tokens.stream().map(CharSequence::toString).collect(Collectors.toList()), preprocessedS2Tokens.stream().map(CharSequence::toString).collect(Collectors.toList()));

        } else {
            throw new InvalidAttributeValueException("A string is null!");
        }
        return duplicateList;
    }

    private String generateStringToSearch(List<CharSequence> preprocessedS1Tokens, int index, int numberOfDuplicateTokens) {
        StringBuilder stringToSearch = new StringBuilder();
        for (int i = index; i < preprocessedS1Tokens.size() && i < index + numberOfDuplicateTokens; i++) {
            stringToSearch.append(preprocessedS1Tokens.get(i)).append(" ");
        }
        return stringToSearch.toString().trim();
    }


    List<DuplicateTextFragment> detectDuplicateNew(List<String> preprocessedS1Tokens, List<String> preprocessedS2Tokens) {
        List<DuplicateTextFragment> duplicateList = new ArrayList();
        int index = 0;
        // Iterate over text.
        while (index < preprocessedS1Tokens.size() - minDuplicateLength + 1) {
            int internalIndex = 0;
            // Get Lists of text based on the minDuplicateLength
            List<String> sequenceToCheck = preprocessedS1Tokens.subList(index, index + minDuplicateLength);
            List<String> sequenceToCheckAgainst = preprocessedS2Tokens.subList(internalIndex, Math.min(internalIndex + bandLength, preprocessedS2Tokens.size()));


            while (calculateScore(sequenceToCheck, sequenceToCheckAgainst) <= MIN_SIMILARITY && internalIndex < preprocessedS2Tokens.size() - minDuplicateLength + 1) {
                sequenceToCheckAgainst = preprocessedS2Tokens.subList(internalIndex, Math.min(internalIndex + bandLength, preprocessedS2Tokens.size()));
                internalIndex++;
            }

            if (calculateScore(sequenceToCheck, sequenceToCheckAgainst) >= MIN_SIMILARITY) {
               // sequenceToCheck.remove(sequenceToCheck.size()-1);
                duplicateList.add(new DuplicateTextFragment(
                        String.join(" ", preprocessedS1Tokens),
                        String.join(" ", preprocessedS2Tokens),
                        preprocessedS2Tokens.subList(0, internalIndex).parallelStream().mapToInt(e -> e.length() +1).sum(),
                        sequenceToCheckAgainst.parallelStream().mapToInt(e -> e.length() +1).sum() -1,
                        calculateScore(sequenceToCheck, sequenceToCheckAgainst)));
                index+= minDuplicateLength;
            }
            index++;
        }
        return duplicateList;
    }

    // Check if the words are present in the same sequence with minor deviation k allowed.
    private double calculateScore(List<String> sequenceToCheck, List<String> sequenceToCheckAgainst) {
        double count = 0.;
        for (String toCheck : sequenceToCheck) {
            count += sequenceToCheckAgainst.contains(toCheck) ? 1 : 0;
        }
        return (count / (sequenceToCheck.size()));
    }



    List<DuplicateTextFragment> detectDuplicateOld(List preprocessedS1Tokens, String s1, String s2) {
        List<DuplicateTextFragment> duplicateList = new ArrayList();

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
                stringToSearch = this.generateStringToSearch(preprocessedS1Tokens, index, numberOfDuplicateTokens);
                indexOfDuplicate = s2.indexOf(stringToSearch);
                if (!duplicateFound) {
                    duplicateFound = indexOfDuplicate != -1;
                }
                numberOfDuplicateTokens++;
            }
            if (duplicateFound) {
                duplicateList.add(new DuplicateTextFragment(s1, s2, lastIndexOfDuplicate, -1, lastStringToSearch.length()));
                index += numberOfDuplicateTokens - 2; // number of duplicate tokens is one more than found tokens
            }

            index++;

        }
        return duplicateList;
    }

}
