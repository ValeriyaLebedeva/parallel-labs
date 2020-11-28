package laba4;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ExecutorActor extends AbstractActor {

    private String executeTest(ExecuteTest t) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(t.getJsScript());
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(t.getFunctionName(), t.getTest().getParams()).toString();
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ExecuteTest.class, t -> sender().tell(
                        executeTest(t), self())
                )
                .build();
    }
}
