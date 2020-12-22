package laba6;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.Random;

public class StorageActor extends AbstractActor {
    ArrayList<String> servers = new ArrayList<>();
    Random random = new Random();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder
                .create()
                .match(GetServerMsg.class, msg -> {
                    servers.getServers();
                })
                .match(RefreshServersMsg.class, msg -> {
                    getSender().tell(servers.get(random.nextInt(servers.size())), ActorRef.noSender());
                })
                .build();
    }
}
