package com.example.purejava;

import static com.example.purejava.ActionManager.goToPoseAction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    private final String filePath;
    private final List<Action> actions = new ArrayList<>();

    public FileParser(String filePath) {
        this.filePath = filePath;
    }

    public void parse() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String raw;

            while ((raw = br.readLine()) != null) {

                String line = raw.trim();
                if (line.isEmpty()) continue;

                if (line.contains("=")) {
                    handleProperty(line);
                } else {
                    Action action = parseAction(line);
                    if (action != null) {
                        actions.add(action);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleProperty(String line) {
        String[] parts = line.split("=");
        String key = parts[0].trim().replace(".", "_");
        String value = parts[1].trim();

        try {
            if (value.startsWith("pose2d")) {
                String inside = value.substring(
                        value.indexOf("(") + 1,
                        value.lastIndexOf(")")
                );

                String[] nums = inside.split(",");
                double x = Double.parseDouble(nums[0].trim());
                double y = Double.parseDouble(nums[1].trim());
                double heading = Double.parseDouble(nums[2].trim());

                setField(key + "_X", x);
                setField(key + "_Y", y);
                setField(key + "_HEADING", heading);

                System.out.println("[CONFIG] Set " + key + " pose2d -> " + value);
            } else {
                double number = Double.parseDouble(value);
                setField(key, number);
                System.out.println("[CONFIG] Set " + key + " = " + number);
            }
        } catch (Exception e) {
            System.out.println("[CONFIG] Error parsing property: " + line);
            e.printStackTrace();
        }
    }

    private static void setField(String fieldName, double value) {
        try {
            Field field = Property.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setDouble(null, value);
        } catch (NoSuchFieldException e) {
            System.out.println("[CONFIG] Unknown property: " + fieldName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Action parseAction(String line) {

        if (line.startsWith("GO.TO.POSE2D")) {

            String inside = line.substring(
                    line.indexOf("(") + 1,
                    line.lastIndexOf(")")
            );

            String[] nums = inside.split(",");

            double x = Double.parseDouble(nums[0].trim());
            double y = Double.parseDouble(nums[1].trim());
            double heading = Double.parseDouble(nums[2].trim());

            return goToPoseAction(x, y, heading);
        }

        switch (line) {
            case "DELAY.ONE.S":
                return new ActionManager.PrintAction("DELAY.ONE.S");

            case "DELAY.HUNDRED.MS":
                return new ActionManager.PrintAction("DELAY.HUNDRED.MS");

            case "PRINT.TEST":
                return new ActionManager.PrintAction("Test print executed");

            default:
                System.out.println("[UNKNOWN COMMAND] " + line);
                return null;
        }
    }

    public List<Action> getActions() {
        return actions;
    }

}
