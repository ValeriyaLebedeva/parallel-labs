package laba6;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;

import java.io.IOException;

public class Anonymizer {
    public static int PORT;
    public static void main(String[] argv) throws IOException {
        ActorSystem actorSystem = ActorSystem.create("routes");
        Http http = Http.get(actorSystem);
        ActorRef storage = actorSystem.actorOf(Props.create(StorageActor.class));
        PORT = Integer.parseInt(argv[0]);
        Zoo zoo = new Zoo(storage);
    }
}
