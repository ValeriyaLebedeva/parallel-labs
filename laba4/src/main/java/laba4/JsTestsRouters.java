package laba4;

import java.time.Duration;

import akka.util.Timeout;
import scala.concurrent.Future;


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
    private final ActorRef storageActor;
    private final static Timeout timeout = Timeout.create(Duration.ofSeconds(5));
    ActorSystem system;


    public JsTestsRouters(ActorSystem system, ActorRef storageActor) {
        this.storageActor = storageActor;
        this.system = system;
    }

    public Route getRouters() {
        return pathPrefix("test", () ->
                concat(
                        pathPrefix("execute", () ->
                                post(() ->
                                        entity(
                                                Jackson.unmarshaller(ExecuteMessage.class),
                                                msg -> {
                                                    Patterns.ask(storageActor, msg, timeout);
                                                    return complete("Executed");
                                                }

                                        ))),
                        pathPrefix("result", () ->
                                path(PathMatchers.segment(), (String packageId) ->
                                        get(() -> {
                                            Future<Object> future = Patterns.ask(storageActor, packageId, timeout);
                                            return completeOKWithFuture(future, Jackson.marshaller());
                                                }
                                        )
                                )
                        )
                )
        );
    }

}
