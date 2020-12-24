package laba6;

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
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CompletionStage;
import static akka.http.javadsl.server.Directives.*;


public class Anonymizer {
    private static final String HOST = "localhost";
    private static final String QUERY_URL = "url";
    private static final String QUERY_COUNT = "count";
    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    public static int port;
    public static Http http;
    private static ActorRef storageActor;
    private static final Random random = new Random();
    public static final String ZOOKEEPER_ADDRESS = "localhost:2181";

    public static void main(String[] argv) throws IOException, KeeperException, InterruptedException {
        ActorSystem actorSystem = ActorSystem.create("routes");
        http = Http.get(actorSystem);
        storageActor = actorSystem.actorOf(Props.create(StorageActor.class));
        if (argv.length > 0) {
            port = Integer.parseInt(argv[0]);
        }
        else {
            port = 2000 + random.nextInt(4000);
        }
        System.out.printf("Port %d was chosen randomly: %d\n", port);
        Zoo zoo = new Zoo(storageActor, ZOOKEEPER_ADDRESS);
        zoo.init(String.valueOf(port));
        System.out.printf("Connected to zookeeper on the: %s", ZOOKEEPER_ADDRESS);
        final ActorMaterializer materializer = ActorMaterializer.create(actorSystem);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                createRoute().flow(actorSystem, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HOST, port),
                materializer
        );
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound ->{
                    actorSystem.terminate();
                });
    }

    private static Route createRoute() {
        return route(get(() ->
                parameter(QUERY_URL, url ->
                        parameter(QUERY_COUNT, c -> {
                            int count = Integer.parseInt(c);
                            if (count <= 0) {
                                System.out.printf("Real Port was: %d", port);
                                return completeWithFuture(fetch(url));
                            }
                            System.out.printf("Intermediate Port was: %d (%d)", port, count);
                            return completeWithFuture(Patterns.ask(storageActor, new GetServerMsg(), TIMEOUT)

                                    .thenApply(nextPort -> (String)nextPort)
                                    .thenCompose(nextPort ->
                                            fetch(
                                                    String.format(
                                                            "http://%s:%s?url=%s&count=%d",
                                                            HOST, nextPort, url, count - 1)
                                            )
                                    )
                            );
                        })
                ))
        );
    }

    private static CompletionStage<HttpResponse> fetch(String url) {
        return http.singleRequest(HttpRequest.create(url));
    }
}
