package com.example.purejava.actions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry for MetaActions. Handles both primitive "Mini Actions" and 
 * user-defined "Big Actions" parsed from text files.
 */
public class MetaActionRegistry {
    private static final Map<String, MetaAction> registry = new HashMap<>();

    static {
        // Register Primitives (Mini Actions)
        register(new MiniAction("GO.TO.POSE2D", params -> {
            String[] nums = params.split(",");
            double x = Double.parseDouble(nums[0].trim());
            double y = Double.parseDouble(nums[1].trim());
            double h = Double.parseDouble(nums[2].trim());
            return ActionManager.goToPoseAction(x, y, h);
        }));
        register(new MiniAction("PRINT", ActionManager.PrintAction::new));
    }

    public static void register(MetaAction action) {
        registry.put(action.getIdentifier().toUpperCase(), action);
    }

    public static Action createAction(String line) {
        line = line.trim();
        int firstParen = line.indexOf("(");
        String name;
        String params = "";

        if (firstParen != -1 && line.endsWith(")")) {
            name = line.substring(0, firstParen).trim();
            params = line.substring(firstParen + 1, line.lastIndexOf(")")).trim();
        } else {
            name = line;
        }

        MetaAction meta = registry.get(name.toUpperCase());
        if (meta == null) return null;
        return meta.create(params);
    }

    /**
     * Parses the MetaActionSettings file to define "Big Actions" composed of Mini Actions.
     */
    public static void loadSettings(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            
            parseActionDefinitions(content.toString());
            
        } catch (IOException e) {
            System.err.println("Error loading MetaActionSettings: " + e.getMessage());
        }
    }

    private static void parseActionDefinitions(String content) {
        int index = 0;
        while ((index = content.indexOf("={", index)) != -1) {
            // Find action name
            int startOfName = content.lastIndexOf("\n", index);
            if (startOfName == -1) startOfName = 0;
            String actionName = content.substring(startOfName, index).trim();
            
            // Find end of block
            int endOfBlock = content.indexOf("}", index);
            if (endOfBlock == -1) break;
            
            String blockContent = content.substring(index + 2, endOfBlock).trim();
            List<String> subActionLines = splitByTopLevelCommas(blockContent);
            
            register(new BigAction(actionName, subActionLines));
            index = endOfBlock + 1;
        }
    }

    private static List<String> splitByTopLevelCommas(String content) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int parenLevel = 0;
        
        for (char c : content.toCharArray()) {
            if (c == '(') parenLevel++;
            if (c == ')') parenLevel--;
            
            if (c == ',' && parenLevel == 0) {
                String s = current.toString().trim();
                if (!s.isEmpty()) result.add(s);
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        String s = current.toString().trim();
        if (!s.isEmpty()) result.add(s);
        
        return result;
    }

    public static List<String> getRegisteredIdentifiers() {
        return new ArrayList<>(registry.keySet());
    }
}
