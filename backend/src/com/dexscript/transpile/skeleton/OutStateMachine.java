package com.dexscript.transpile.skeleton;

import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OutStateMachine {

    private int state;
    private Map<Integer, Map<String, Integer>> transitions = new HashMap<>();
    private Set<Integer> yieldToStates = new HashSet<>();

    public int nextState() {
        state += 1;
        return state;
    }

    public int state() {
        return state;
    }

    public void addTransition(int fromState, String producer, int toState) {
        transitions
                .computeIfAbsent(fromState, k -> new HashMap<>())
                .putIfAbsent(producer, toState);
    }

    public void addYieldToState(int state) {
        yieldToStates.add(state);
    }

    public void yieldTo(Gen g, int yieldToState) {
        g.__("yieldTo("
        ).__(yieldToState
        ).__(new Line(");"));
        addYieldToState(yieldToState);
    }

    public void genResumeMethods(Gen g) {
        genResume(g);
        for (Map.Entry<Integer, Map<String, Integer>> entry : transitions.entrySet()) {
            genResumeFrom(g, entry.getKey(), entry.getValue());
        }
    }

    private void genResumeFrom(Gen g, Integer fromState, Map<String, Integer> toStates) {
        g.__("public void resume__from__"
        ).__(fromState
        ).__("() {"
        ).__(new Indent(() -> {
            for (Map.Entry<String, Integer> entry : toStates.entrySet()) {
                g.__("if (((Promise)"
                ).__(entry.getKey()
                ).__(").finished()) {"
                ).__(new Indent(() -> {
                    Integer toState = entry.getValue();
                    g.__("State"
                    ).__(toState
                    ).__("__(); return;");
                })).__(new Line("}"));
            }
        })).__(new Line("}"));
    }

    private void genResume(Gen g) {
        g.__("public void resume() {"
        ).__(new Indent(() -> {
            if (!yieldToStates.isEmpty()) {
                genSwitchYieldToStates(g);
            }
            if (!transitions.isEmpty()) {
                genSwitchTransitions(g);
            }
        })).__(new Line("}"));
    }

    private void genSwitchYieldToStates(Gen g) {
        g.__(new Line("int yieldToState = resetYield();"));
        g.__("if (yieldToState != -1) {"
        ).__(new Indent(() -> {
            g.__("switch (yieldToState) {"
            ).__(new Indent(() -> {
                for (Integer yieldToState : yieldToStates) {
                    g.__("case "
                    ).__(yieldToState
                    ).__(": "
                    ).__(OutStateMethod.methodName(yieldToState)
                    ).__(new Line("(); return;"));
                }
            }
            )).__(new Line("}"));
        }
        )).__(new Line("}"));
    }

    private void genSwitchTransitions(Gen g) {
        g.__("switch (Get__state()) {"
        ).__(new Indent(() -> {
            for (Integer fromState : transitions.keySet()) {
                g.__("case "
                ).__(fromState
                ).__(": resume__from__"
                ).__(fromState
                ).__(new Line("(); return;"));
            }
        })).__("}");
    }
}
