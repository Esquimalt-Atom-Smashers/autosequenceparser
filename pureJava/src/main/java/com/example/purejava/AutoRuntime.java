package com.example.purejava;

import java.util.ArrayList;
import java.util.List;
import com.example.purejava.FileParser;
import java.lang.reflect.Field;


public class AutoRuntime {
    FileParser fileParser;

    public static void main(String[] args) {
        AutoRuntime runtime = new AutoRuntime();
        runtime.run("pureJava/src/main/java/com/example/purejava/RedCloseAuto");
    }

    public void run(String path) {
        fileParser = new FileParser(path);
        fileParser.parse();

        List<Action> actions = fileParser.getActions();

        runActions(actions);
        Property.reset();
    }

    private void dumpAllProperties() {
        System.out.println("--- All Properties ---");
        for (Field field : Property.class.getDeclaredFields()) {
            try {
                System.out.println(field.getName() + " = " + field.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void runActions(List<Action> actions) {
        System.out.println("--- Running Actions ---");
        for (Action action : actions) {
            action.run();
        }
    }
}
