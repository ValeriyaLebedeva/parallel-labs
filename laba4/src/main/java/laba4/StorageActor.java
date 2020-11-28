package laba4;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.Patterns;
import akka.routing.BalancingPool;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;


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
                this.responseMsg = String.format("%s: Expected: %s, but received: %s",
                        testName, expectedResult, result);
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
        ActorRef executorActors = system.actorOf(new BalancingPool(5).props(
                Props.create(ExecutorActor.class)), "testAggregator");
       ArrayList<Result> results = new ArrayList<>();
        try {
            System.out.println(msg.getTests().size());
            for (Test t: msg.getTests()) {
                Future<Object> future = Patterns.ask(executorActors, new ExecuteTest(t, msg.getJsScript(), msg.getFunctionName()), timeout);
                Result result;
                result = new Result(t.getTestName(), (String) Await.result(future, timeout.duration()), t.getExpectedResult());
                System.out.printf("Executed test %s, result: %s%n", t.getTestName(), result);
                results.add(result);
            }
        } catch (Exception e) {
            return e.toString();
        }
        storage.put(msg.getPackageId(), results);
        return "Executed";
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
