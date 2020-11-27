package laba4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ExecuteMessage {
    public final String packageId;
    public final String jsScript;
    public final String functionName;
    public final ArrayList<Test> tests;
    @JsonCreator
    public ExecuteMessage(@JsonProperty("packageId") String packageId, @JsonProperty("jsScript") String jsScript, @JsonProperty("functionName") String functionName, @JsonProperty("tests") ArrayList<Test> tests) {
        this.packageId = packageId;
        this.jsScript = jsScript;
        this.functionName = functionName;
        this.tests = tests;
    }
}