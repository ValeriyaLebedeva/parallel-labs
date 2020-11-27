package laba4;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class StorageActor extends AbstractActor {
    private Map<String, ArrayList<Result>> storage = new HashMap<>();

    public static class Results {
        
    }

    public static class Result {
        String testName;
        String responseMsg;

        public Result(String testName, String result, String expectedResult) {
            this.testName = testName;
            if (result.equals(expectedResult)) {
                this.responseMsg = "OK";
            } else {
                this.responseMsg = String.format("Expected: %s, but received: %s",
                        expectedResult, result);
            }
        }
    }


    private String executeTests(ExecuteMessage msg) {
        return "OK";
    }

    private String getResults(String id) {
        if (!storage.containsKey(id)) {
            return "No such packageId in storage";
        } else {
            return
        }
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ExecuteMessage.class, msg -> sender().tell(
                        executeTests(msg), self())
                )
                .match(String.class, id -> sender().tell(
                        getResults(id), self())
                )
                .build();
    }

}
