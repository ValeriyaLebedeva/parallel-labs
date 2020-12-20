package laba5;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;


public class CashingActor extends AbstractActor {
    private final HashMap<String, Long> storage = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(MessageGetResult.class, msg -> {
                    getSender().tell(storage.getOrDefault(msg.getUrl(), ));
                })
                .match(MessageTest.class, msg -> {

                })
                .build();
    }

}
