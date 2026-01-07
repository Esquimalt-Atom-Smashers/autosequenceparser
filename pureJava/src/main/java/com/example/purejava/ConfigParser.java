package com.example.purejava;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConfigParser {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\User\\Documents\\GitHub\\autosequenceparser\\pureJava\\src\\main\\java\\com\\example\\purejava\\config.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                // =========================
                // PROPERTY (KEY = VALUE)
                // =========================
                if (line.contains("=")) {
                    String[] parts = line.split("=");
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    // pose2d property
                    if (value.startsWith("pose2d")) {
                        String inside = value.substring(
                                value.indexOf("(") + 1,
                                value.indexOf(")")
                        );

                        String[] nums = inside.split(",");
                        double x = Double.parseDouble(nums[0].trim());
                        double y = Double.parseDouble(nums[1].trim());
                        double heading = Double.parseDouble(nums[2].trim());

                        System.out.println(key + " -> pose2d");
                        System.out.println("  x: " + x);
                        System.out.println("  y: " + y);
                        System.out.println("  heading: " + heading);
                    }
                    // numeric property
                    else {
                        if (value.contains(".")) {
                            double number = Double.parseDouble(value);
                            System.out.println(key + " -> " + number);
                        } else {
                            int number = Integer.parseInt(value);
                            System.out.println(key + " -> " + number);
                        }
                    }
                }

                // =========================
                // AUTO COMMANDS
                // =========================
                else if (line.equals("FAR.SHOOT.POS")) {
                    System.out.println("I am doing FAR.SHOOT.POS command");
                }

                else if (line.startsWith("GO.TO.POSE2D")) {
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
                // TYPO / UNKNOWN COMMAND
                // =========================
                else {
                    System.out.println(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
