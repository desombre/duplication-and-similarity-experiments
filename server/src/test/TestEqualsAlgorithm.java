package test;


import algorithms.SimpleEquals;
import org.junit.Test;

public class TestEqualsAlgorithm extends TestSimilarityAlgorithm {

    @Test
    public void test(){
        super.testAlgorithm(new SimpleEquals());
    }

}
