package laba4;

import com.example.UserRegistry;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsTestsStorage extends AbstractBehavior<JsTestsStorage.Command>  {

    interface Command {}

    public final static class ExecuteProgram implements JsTestsStorage.Command {
        public final JsTestsStorage.Test user;
        public final ActorRef<UserRegistry.ActionPerformed> replyTo;
        public CreateUser(UserRegistry.User user, ActorRef<UserRegistry.ActionPerformed> replyTo) {
            this.user = user;
            this.replyTo = replyTo;
        }
    }

    public final static class Test {
        public final String packageId;
        public final String jsScript;
        public final String functionName;
        public final String tests;
        @JsonCreator
        public Test(@JsonProperty("packageId") String packageId, @JsonProperty("jsScript") String jsScript, @JsonProperty("functionName") String functionName, @JsonProperty("tests") String tests) {
            this.packageId = packageId;
            this.jsScript = jsScript;
            this.functionName = functionName;
            this.tests = tests;
        }
    }

}
