package laba4;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletionStage;

public class JsScriptTester {
    final static String ADDRESS = "localhost";
    final static String PORT = "8080";

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("JsScriptTesting");
        ActorRef storageActor = system.actorOf(Props.create(StoreActor.class), "store");
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        router = JsTestsRouters(system, storageActor);
        TestingServer server = new TestingServer(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = server.getRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow, ConnectHttp.toHost(IP_ADDR, PORT), materializer
        );

        System.out.printf("Server listening on %s:%d\n", IP_ADDR, PORT);
        System.out.println("Press ENTER to exit\n");
        System.in.read();
        binding.thenCompose(ServerBinding::unbind).thenAccept(unbound -> system.terminate());
    }

}
