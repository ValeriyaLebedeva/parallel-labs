package laba4;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ExecutorActor extends AbstractActor {

    private String executeTest(Test t) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(t.);
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(functionName, params).toString();
        return "";
    }

    @Override
    public Receive createReceive() {
        ReceiveBuilder.create()
                .match(Test.class, t -> sender().tell(
                        executeTest(t), self())
                )
                .build();
    };
}
