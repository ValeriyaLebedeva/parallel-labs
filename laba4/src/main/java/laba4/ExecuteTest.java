package laba4;

public class ExecuteTest {
    private final Test test;
    private final String jsScript;
    public final String functionName;

    public ExecuteTest(Test test, String jsScript, String functionName) {
        this.test = test;
        this.jsScript = jsScript;
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Test getTest() {
        return test;
    }

    public String getJsScript() {
        return jsScript;
    }
}
