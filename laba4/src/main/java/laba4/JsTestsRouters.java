package laba4;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;

import akka.pattern.Patterns;
import com.example.UserRegistry.User;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import akka.http.javadsl.marshallers.jackson.Jackson;

import static akka.http.javadsl.server.Directives.*;

import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import laba4.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Routes can be defined in separated classes like shown in here
 */
//#user-routes-class
public class JsTestsRouters {
    //#user-routes-class
    private final static Logger log = LoggerFactory.getLogger(JsTestsStorage.class);
    private final ActorRef storageActor;
    private final Duration askTimeout;
    private final Scheduler scheduler;

    public JsTestsRouters(ActorSystem system, ActorRef storageActor) {
        this.storageActor = storageActor;
        scheduler = system.scheduler();
        askTimeout = system.settings().config().getDuration("my-app.routes.ask-timeout");
    }

    public Route jsTestsRoutes() {
        return pathPrefix("testjsscript", () ->
                concat(
                        pathPrefix("execute", () ->
                                post(() ->
                                        entity(
                                                Jackson.unmarshaller(Message.class),
                                                msg -> {
                                                    Patterns.ask(storageActor, msg, askTimeout);
                                                    return complete("Executed");
                                                }

                                        ))),
                        pathPrefix("getresult", () ->
                                concat(
                                        pathEnd(() ->
                                                get(() -> {

                                                        }
                                                )
                                        ),
                                        path(PathMatchers.segment(), (String packageId) ->
                                                get(() ->
                                                        rejectEmptyResponse(() ->
                                                                onSuccess(getResult(packageId), performed ->
                                                                        complete(StatusCodes.OK, performed.maybeResult, Jackson.marshaller())
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

}
