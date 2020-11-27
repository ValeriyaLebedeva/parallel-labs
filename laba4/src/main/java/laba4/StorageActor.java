package laba4;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class StorageActor extends AbstractActor {

    public static class Result {
        result
    }

    private Map<String, ArrayList<Result>> storage = new HashMap<>();;

    private String executeTests(ExecuteMessage msg) {
        return "OK";
    }

    private String getResults(String id) {
        return "Some result";
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
