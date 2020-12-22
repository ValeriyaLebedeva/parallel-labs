package laba6;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;

public class StorageActor extends AbstractActor {
    ArrayList<String> servers = new ArrayList<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder
                .create()
                .match(GetServerMsg.class, msg -> {
                    servers.getServers();
                })
                .match(RefreshServersMsg.class, msg -> {
                    getSender().tell(servers.get(rand.nextInt(servers.size())), ActorRef.noSender());
                })
                .build();
    }
}
