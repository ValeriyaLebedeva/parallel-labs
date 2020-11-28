package laba4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Test {
    private final String testName;
    private final String expectedResult;
    private final ArrayList<Integer> params;

    @JsonCreator
    public Test(@JsonProperty("tesName") String testName, @JsonProperty("expectedResult") String expectedResult, @JsonProperty("tests") ArrayList<Integer> params) {
        this.testName = testName;
        this.expectedResult = expectedResult;
        this.params = params;
    }

    public String getTestName() {
        return testName;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public ArrayList<Integer> getParams() {
        return params;
    }
}
