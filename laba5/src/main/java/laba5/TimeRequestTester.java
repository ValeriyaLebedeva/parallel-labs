package laba5;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.util.Timeout;
import java.time.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;

public class TimeRequestTester {
    private static  final Duration TIMEOUT = Duration.ofSeconds(2);

    public static void main(String[] args) throws IOException {
        System.out.println("start!");
        ActorSystem system = ActorSystem.create("routes");
        final Http http = Http.get(system);
        ActorRef ActorCashing = system.actorOf(Props.create(CashingActor.class));
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = createFlow(materializer, ActorCashing);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost("localhost", 8080),
                materializer
        );
        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }

    private static Flow<HttpRequest, HttpResponse, NotUsed> createFlow(ActorMaterializer materializer, ActorRef actorCashing) {
        return Flow.of(HttpRequest.class)
                .map(r -> {
                    Query q = r.getUri().query();
                    Integer count = Integer.parseInt(q.get("count").get());
                    System.out.println(count);
                    return new Pair<>(q.get("testUrl").get(), count);
                })
                .mapAsync(1, (Pair<String, Integer> pair) -> {
                    CompletionStage<Object> stage = Patterns.ask(actorCashing, new MessageGetResult(pair.getKey()), TIMEOUT);
                    return stage.thenCompose((Object re))
                })
    }

}
