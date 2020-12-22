package laba6;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;

public class Anonimaizer {
    public static int PORT;
    public static void main(String[] argv) {
        ActorSystem actorSystem = ActorSystem.create("routes");
        Http http = Http.get(actorSystem);
        actorSystem.actorOf(Props.create(StorageActor.class));
        PORT = Integer.parseInt(argv[0]);
    }
}
