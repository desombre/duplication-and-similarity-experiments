package controller;

import algorithms.AllAlgorithmsList;
import algorithms.DuplicationTextUtil;
import algorithms.StringSimilarityAlgorithm;
import data.DuplicateTextFragment;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                                      @RequestParam(name = "s2") String s2) {
        List<DuplicateTextFragment> duplicationResults = DuplicationTextUtil.detectDuplicateTextFragments(s1, s2);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(duplicationResults);


    }
}
