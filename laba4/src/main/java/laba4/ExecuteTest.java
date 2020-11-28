package laba4;

public class ExecuteTest {
    private final Test test;
    private final String jsScript;

    public ExecuteTest(Test test, String jsScript) {
        this.test = test;
        this.jsScript = jsScript;
    }

    public Test getTest() {
        return test;
    }

    public String getJsScript() {
        return jsScript;
    }
}
