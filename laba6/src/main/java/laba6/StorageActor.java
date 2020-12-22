package laba6;

import akka.actor.AbstractActor;

import java.util.ArrayList;

public class StorageActor extends AbstractActor {
    ArrayList<String> servers = new ArrayList<>();

    @Override
    public Receive createReceive() {
        return null;
    }
}
