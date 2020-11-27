package laba4;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.util.concurrent.CompletionStage;

public class JsScriptTester {
    final static String ADDRESS = "localhost";
    final static int PORT = 8080;

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("JsScriptTesting");
        ActorRef storageActor = system.actorOf(Props.create(StorageActor.class), "store");
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        Route router = new JsTestsRouters(system, storageActor).getRouters();
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = router.flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow, ConnectHttp.toHost(ADDRESS, PORT), materializer
        );

        System.out.printf("Server listening on %s:%d\n", ADDRESS, PORT);
        System.out.println("Press ENTER to exit\n");
        System.in.read();
        binding.thenCompose(ServerBinding::unbind).thenAccept(unbound -> system.terminate());
    }

}
