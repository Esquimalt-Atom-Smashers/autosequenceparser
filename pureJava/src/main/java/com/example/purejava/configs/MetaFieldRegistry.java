package com.example.purejava.configs;

import com.example.purejava.configs.types.IntakeSetting;
import com.example.purejava.configs.types.MetaField;
import com.example.purejava.configs.types.Pose2d;

import java.util.HashMap;
import java.util.Map;

public class MetaFieldRegistry {
    
    public static class ConfigEntry<T> {
        public final String fieldName;
        public final Class<T> type;
        public T value;

        public ConfigEntry(String fieldName, Class<T> type, T defaultValue) {
            this.fieldName = fieldName;
            this.type = type;
            this.value = defaultValue;
        }
    }

    private static final Map<String, ConfigEntry<?>> entries = new HashMap<>();
    private static final Map<Class<?>, MetaField<?>> typeDefinitions = new HashMap<>();

    static {
        // Register type definitions
        registerType(new Pose2d(0, 0, 0));
        registerType(new IntakeSetting("", false, 0));

        // Register specific fields with default values
        registerField("maxPower", Double.class, 0.0);
        registerField("redGoalPose", Pose2d.class, new Pose2d(-72, 48, 0));
        registerField("blueGoalPose", Pose2d.class, new Pose2d(72, 48, 0));
        registerField("intakeActive", IntakeSetting.class, new IntakeSetting("NORMAL", true, 0.8));
        registerField("motorName", String.class, "motorName");
//        registerField("aprilTagPose", Pose3d.class, new Pose3d(new Pose2d(-72, 48, 0), 30));
//        To be documented: line above does not work within current scope
    }

    public static void registerType(MetaField<?> typeDef) {
        // Find the actual class that implements MetaField
        // For simplicity, we assume the class itself is the type
        typeDefinitions.put(typeDef.getClass(), typeDef);
    }

    public static <T> void registerField(String fieldName, Class<T> type, T defaultValue) {
        entries.put(fieldName.toLowerCase(), new ConfigEntry<>(fieldName, type, defaultValue));
    }

    public static ConfigEntry<?> getEntry(String fieldName) {
        return entries.get(fieldName.toLowerCase());
    }

    public static MetaField<?> getTypeDefinition(Class<?> type) {
        return typeDefinitions.get(type);
    }
}
