package com.example.purejava.actions;

import java.util.ArrayList;
import java.util.List;

public class BigAction implements MetaAction {
    private final String id;
    private final List<String> subActionLines;

    public BigAction(String id, List<String> subActionLines) {
        this.id = id;
        this.subActionLines = subActionLines;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public Action create(String params) {
        List<Action> actions = new ArrayList<>();
        for (String line : subActionLines) {
            Action a = MetaActionRegistry.createAction(line);
            if (a != null) {
                actions.add(a);
            }
        }
        return () -> {
            for (Action a : actions) {
                a.run();
            }
            return false;
        };
    }
}
