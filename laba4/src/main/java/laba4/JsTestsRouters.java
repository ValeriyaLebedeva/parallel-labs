package laba4;

import java.time.Duration;

import akka.pattern.Patterns;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import akka.http.javadsl.marshallers.jackson.Jackson;

import static akka.http.javadsl.server.Directives.*;

import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
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
                                                Jackson.unmarshaller(ExecuteMessage.class),
                                                msg -> {
                                                    Patterns.ask(storageActor, msg, askTimeout);
                                                    return complete("Executed");
                                                }

                                        ))),
                        pathPrefix("getresult", () ->
                                path(PathMatchers.segment(), (String packageId) ->
                                        get(() -> {
                                            Patterns.ask(storageActor, packageId, askTimeout);
                                            return complete("Executed");
                                                }
                                        )
                                )
                        )
                )
        );
    }

}
