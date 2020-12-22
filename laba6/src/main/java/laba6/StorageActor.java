package laba6;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;

public class StorageActor extends AbstractActor {
    ArrayList<String> servers = new ArrayList<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder
                .create()
                .match(GetServerMsg.class, msg -> {
                    return;
                })
                .match(RefreshServersMsg.class, msg -> {
                    return;
                })
                .build();
    }
}
