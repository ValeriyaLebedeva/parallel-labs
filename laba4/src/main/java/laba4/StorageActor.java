package laba4;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class StorageActor extends AbstractActor {
    private final Map<String, ArrayList<Result>> storage = new HashMap<>();


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
