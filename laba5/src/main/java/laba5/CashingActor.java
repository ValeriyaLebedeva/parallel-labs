package laba5;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;


public class CashingActor extends AbstractActor {
    private final HashMap<String, Float> storage = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(MessageGetResult.class, msg -> {
                    getSender().tell(storage.getOrDefault(msg.getUrl(), (float)-1.0), ActorRef.noSender());
                })
                .match(MessageTest.class, msg -> {
                    storage.put(msg.getUrl(), msg.getTime());
                })
                .build();
    }

}
