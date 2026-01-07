package com.example.purejava;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class PropertyLoader {

    static String configPath = "C:\\Users\\User\\Documents\\GitHub\\autosequenceparser\\pureJava\\src\\main\\java\\com\\example\\purejava\\config.txt";
    static String activeAutoPath = "C:\\Users\\User\\Documents\\GitHub\\autosequenceparser\\pureJava\\src\\main\\java\\com\\example\\purejava\\ActiveAuto.txt";

    public static void main(String[] args) {
        loadAuto();
    }
    // =========================
    // TELEOP LOADING
    // =========================
    public static void loadTeleOp() {
        loadFile(configPath, false);

        Field[] fields = Properties.class.getDeclaredFields();

        for (Field field : fields) {
            try {
                String key = field.getName();
                Object value = field.get(null); // null because fields are static
                System.out.println(key + " = " + value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    // =========================
    // AUTO LOADING
    // =========================
    public static void loadAuto() {
        // Load base config first
        loadTeleOp();
        // Then override with auto config
        loadFile(activeAutoPath, true);

        Field[] fields = Properties.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                String key = field.getName();
                Object value = field.get(null); // null because fields are static
                System.out.println(key + " = " + value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    // =========================
    // SHARED FILE LOADER
    // =========================
    private static void loadFile(String filePath, boolean allowCommands) {
        System.out.println("I am getting " + filePath + " .Allow commands: " + allowCommands);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                // =========================
                // PROPERTY LINE
                // =========================
                if (line.contains("=")) {
                    String[] parts = line.split("=");
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    // pose2d override
                    if (value.startsWith("pose2d")) {
                        String inside = value.substring(
                                value.indexOf("(") + 1,
                                value.indexOf(")")
                        );

                        String[] nums = inside.split(",");
                        double x = Double.parseDouble(nums[0].trim());
                        double y = Double.parseDouble(nums[1].trim());
                        double heading = Double.parseDouble(nums[2].trim());

                        setField("RED_GOAL_X", x);
                        setField("RED_GOAL_Y", y);
                        setField("RED_GOAL_HEADING", heading);

                        if (allowCommands) {
                            System.out.println("Override pose2d -> x:" + x +
                                    " y:" + y + " heading:" + heading);
                        }
                    }
                    // numeric property
                    else {
                        double number = Double.parseDouble(value);
                        String fieldName = key.replace(".", "_");

                        setField(fieldName, number);

                        if (allowCommands) {
                            System.out.println("Override " + fieldName + " = " + number);
                        }
                    }
                }

                // =========================
                // AUTO COMMANDS
                // =========================
                else if (allowCommands && line.equals("FAR.SHOOT.POS")) {
                    System.out.println("I am doing FAR.SHOOT.POS command");
                }

                else if (allowCommands && line.startsWith("GO.TO.POSE2D")) {
                    String inside = line.substring(
                            line.indexOf("(") + 1,
                            line.indexOf(")")
                    );

                    String[] nums = inside.split(",");
                    double x = Double.parseDouble(nums[0].trim());
                    double y = Double.parseDouble(nums[1].trim());
                    double heading = Double.parseDouble(nums[2].trim());

                    System.out.println(
                            "I am going to " + x + "," + y + "," + heading
                    );
                }

                // =========================
                // UNKNOWN / TYPO
                // =========================
                else if (allowCommands) {
                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // REFLECTION SETTER
    // =========================
    private static void setField(String fieldName, double value) {
        try {
            Field field = Properties.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setDouble(null, value);
        } catch (NoSuchFieldException e) {
            System.out.println("Unknown property: " + fieldName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
