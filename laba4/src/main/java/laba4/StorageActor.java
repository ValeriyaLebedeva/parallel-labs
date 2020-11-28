package laba4;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Future;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class StorageActor extends AbstractActor {
    private final Map<String, ArrayList<Result>> storage = new HashMap<>();
    private final static Timeout timeout = Timeout.create(Duration.ofSeconds(5));

    public static class Result {
        private final String testName;
        private final String responseMsg;

        public Result(String testName, String result, String expectedResult) {
            this.testName = testName;
            if (result.equals(expectedResult)) {
                this.responseMsg = "OK";
            } else {
                this.responseMsg = String.format("Expected: %s, but received: %s",
                        expectedResult, result);
            }
        }

        public String getTestName() {
            return testName;
        }

        public String getResponseMsg() {
            return responseMsg;
        }
    }

    private String executeTests(ExecuteMessage msg) {
        ActorSystem system = ActorSystem.create("ExecuteTesting");
        for (Test t: msg.getTests()) {
            ActorRef executorActor = system.actorOf(Props.create(ExecutorActor.class), t.getTestName());
            Future<Object> future = Patterns.ask(executorActor, new ExecuteTest(t, msg.getJsScript(), msg.getFunctionName()), timeout);
        }
        return "OK";
    }


    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ExecuteMessage.class, msg -> sender().tell(
                        executeTests(msg), self())
                )
                .match(String.class, id -> {
                            if (!storage.containsKey(id)) {
                                sender().tell("No such packageId in storage", self());
                            } else {
                                sender().tell(storage.get(id), self());
                            }
                        }
                )
                .build();
    }

}
