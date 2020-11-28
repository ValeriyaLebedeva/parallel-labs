package laba4;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import akka.util.Timeout;
import scala.concurrent.Await;
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
public class Router {
    //#user-routes-class
    private final ActorRef storageActor;
    private final static Timeout timeout = Timeout.create(Duration.ofSeconds(5));
    ActorSystem system;


    public Router(ActorSystem system, ActorRef storageActor) {
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
                                                    Future<Object> future = Patterns.ask(storageActor, msg, timeout);
                                                    String result;
                                                    try {
                                                        result = (String) Await.result(future, timeout.duration());
                                                    } catch (Exception e) {
                                                        return complete(e.toString());
                                                    }
                                                    return complete(result);
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
