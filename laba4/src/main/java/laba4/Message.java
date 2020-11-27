package laba4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    public final String packageId;
    public final String jsScript;
    public final String functionName;
    public final String tests;
    @JsonCreator
    public Message(@JsonProperty("packageId") String packageId, @JsonProperty("jsScript") String jsScript, @JsonProperty("functionName") String functionName, @JsonProperty("tests") String tests) {
        this.packageId = packageId;
        this.jsScript = jsScript;
        this.functionName = functionName;
        this.tests = tests;
    }
}