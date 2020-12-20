package laba5;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;


public class CashingActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(MessageTest.class, msg -> {

                })
    }

}
