package laba4;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ExecutorActor extends AbstractActor {

    private String executeTest(ExecuteTest t)  {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        String result;
        try {
            engine.eval(t.getJsScript());
            Invocable invocable = (Invocable) engine;
            result = invocable.invokeFunction(t.getFunctionName(), t.getTest().getParams().toArray()).toString();
        } catch (Exception e) {
            return e.toString();
        }
        return result;
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
