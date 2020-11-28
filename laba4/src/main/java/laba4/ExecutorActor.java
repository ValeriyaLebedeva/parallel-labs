package laba4;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

public class ExecutorActor extends AbstractActor {

    private String executeTest(Test t) {
        
        return "";
    }

    @Override
    public Receive createReceive() {
        ReceiveBuilder.create()
                .match(Test.class, t -> sender().tell(
                        executeTest(t), self())
                )
                .build();
    };
}
