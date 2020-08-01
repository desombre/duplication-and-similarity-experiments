package controller;

import algorithms.AllAlgorithmsList;
import algorithms.BasicDuplicateDetector;
import algorithms.StringSimilarityAlgorithm;
import com.opencsv.CSVReader;
import data.DuplicateTextFragment;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@EnableAutoConfiguration
public class ControllerImplementation implements Controller {

    @Override
    @GetMapping(path = "/allAlgorithms", produces = "application/json")
    public List<String> getAllImplementedAlgorithms() {
        return AllAlgorithmsList.getList();
    }

    @Override
    @ResponseBody
    @GetMapping(path = "/calculate", produces = "application/json")
    public ResponseEntity calculate(@RequestParam(name = "algorithm") String algorithmName,
                                    @RequestParam(name = "s1") String s1,
                                    @RequestParam(name = "s2") String s2) {
        if (algorithmName.equals("All")) {
            List<String> results = new ArrayList();
            for (String currentAlgorithmName : AllAlgorithmsList.getList()) {
                Optional<StringSimilarityAlgorithm> algorithm = AllAlgorithmsList.getAlgorithm(currentAlgorithmName);
                if (algorithm.isPresent()) {
                    results.add(currentAlgorithmName + ": " + algorithm.get().calculateSimilarity(s1, s2));
                }
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(results);
        } else {
            Optional<StringSimilarityAlgorithm> algorithm = AllAlgorithmsList.getAlgorithm(algorithmName);

            if (algorithm.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(algorithm.get()
                                .calculateSimilarity(s1, s2));
            } else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(null);

            }
        }

    }

    @Override
    @ResponseBody
    @GetMapping(path = "/duplication", produces = "application/json")
    public ResponseEntity duplication(@RequestParam(name = "s1") String s1,
                                      @RequestParam(name = "s2") String s2) throws Exception {
        List<DuplicateTextFragment> duplicationResults = new BasicDuplicateDetector().detectDuplicateTextFragments(s1, s2);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(duplicationResults);
    }


    @Override
    @ResponseBody
    @GetMapping(path = "/timeDuplicate", produces = "application/json")
    public ResponseEntity duplicateTime(@RequestParam(name = "filepath") String csvFile, @RequestParam(name = "index") Integer testIndex) throws Exception {
        List<String> data = readCsv(csvFile).stream().map((entry) -> entry[3]).collect(Collectors.toList());
        System.out.println(data.size());
        StringBuilder response = new StringBuilder();
        response.append("{");
        BasicDuplicateDetector dtu = new BasicDuplicateDetector();

        if (testIndex != null) {
            response.append("\"duration\": ");
            Date startTime = new Date();
            String testSummary = data.get(testIndex);
            for (String summary : data) {
                dtu.detectDuplicateTextFragments(testSummary, summary);
            }


            response.append(new Date().getTime() - startTime.getTime());
        }


        //List<DuplicateTextFragment> duplicationResults = DuplicationTextUtil.detectDuplicateTextFragments(s1, s2);
        response.append("}");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response.toString());

    }

    @Override
    @ResponseBody
    @GetMapping(path = "/calculateQuality", produces = "application/json")
    public ResponseEntity calculateQuality(@RequestParam(name = "filepath") String csvFile, @RequestParam(name = "minLength") int minlength) throws Exception {
        List<String[]> dataSet = readCsv(csvFile);
        String[] headerLine = dataSet.remove(0);
        System.out.println("------------------");
        System.out.println(String.format("Dataset size: %d entries.", dataSet.size()));
        System.out.println(String.format("Headers: %s", Arrays.toString(headerLine)));
        StringBuilder response = new StringBuilder();
        BasicDuplicateDetector dtu = new BasicDuplicateDetector();
        dtu.setMinDuplicateLength(minlength);

        // Json Start
        response.append("{");


        // execute duplicate detection
        for (int i = 0; i < dataSet.size(); i++) {

            if (i % 10 == 0) {
                System.out.println(String.format("Calculated: %d.", i));
                System.out.println(String.format("Response length: %d.", response.length()));
            }

            response.append("\"" + dataSet.get(i)[0] + "\":");

            response.append("{");
            boolean duplicateFound = false;

            for (int j = 0; j < dataSet.size(); j++) {

                if (i == j)
                    continue;
                List<DuplicateTextFragment> detected = dtu.detectDuplicateTextFragments(dataSet.get(i)[6], dataSet.get(j)[6]);
                duplicateFound = !detected.isEmpty() || duplicateFound;
                if (!detected.isEmpty()) {
                    response.append("\"" + j + "\": ");
                    response.append(detected.get(0));
                    response.append(",");

                }

            }
            if (duplicateFound)
                response.deleteCharAt(response.length() - 1);


            //System.out.println((dataEntry[0]));
            response.append("},");

        }
        response.deleteCharAt(response.length() - 1);
        // Json End
        response.append("}");
        System.out.println("------------------");
        PrintWriter out = new PrintWriter("detected_duplicates_" + dtu.getMinDuplicateLength() + "_075.json");
        out.println(response.toString());
        out.close();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response.toString());
    }


    private List<String[]> readCsv(String csvFile) {
        FileReader fr = null;
        List<String[]> data = new ArrayList<String[]>();
        try {

            fr = new FileReader(csvFile);
            CSVReader csvReader = new CSVReader(fr);
            String[] nextRecord;
            //skip header line

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                //System.out.println(nextRecord);
                data.add(nextRecord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
