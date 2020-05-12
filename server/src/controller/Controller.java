package controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface Controller {

    List<String> getAllImplementedAlgorithms();

    ResponseEntity calculate(String algorithmName, String s1, String s2);
    ResponseEntity duplication(String s1, String s2);
}
