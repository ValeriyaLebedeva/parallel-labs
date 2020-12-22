package laba6;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.IncomingConnection;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class Anonymizer {
    private static final String HOST = "localhost";
    public static int PORT;
    public static void main(String[] argv) throws IOException {
        ActorSystem actorSystem = ActorSystem.create("routes");
        Http http = Http.get(actorSystem);
        ActorRef storage = actorSystem.actorOf(Props.create(StorageActor.class));
        PORT = Integer.parseInt(argv[0]);
        Zoo zoo = new Zoo(storage);
        final ActorMaterializer materializer = ActorMaterializer.create(actorSystem);
        final Flow<HttpResponse, HttpRequest, NotUsed> routeFlow =
                createRoute().flow(actorSystem, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HOST, PORT),
                materializer
        );
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound ->{
                    actorSystem.terminate();
                });
    }

    private static IncomingConnection createRoute() {
    }
}
