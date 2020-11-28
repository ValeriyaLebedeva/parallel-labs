package laba4;

public class ExecuteTest {
    private final Test test;
    private final String jsScript;
    private final String name;

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
