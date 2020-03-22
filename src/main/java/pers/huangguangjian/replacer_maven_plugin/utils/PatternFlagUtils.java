package pers.huangguangjian.replacer_maven_plugin.utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Pattern;

public class PatternFlagUtils {
    public static final int NO_FLAGS = -1;

    public static int buildFlags(List<String> flags) {
        if (flags == null || flags.isEmpty()) {
            return NO_FLAGS;
        }

        int value = 0;
        for (String flag : flags) {
            value |= getStaticFieldValueOf(flag);
        }
        return value;
    }

    private static int getStaticFieldValueOf(String fieldName) {
        for (Field f : Pattern.class.getFields()) {
            if (f.getName().equalsIgnoreCase(fieldName)) {
                try {
                    return (Integer) f.get(null);
                } catch (Exception e) {
                    throw new IllegalStateException("Could not access Pattern field: " + f.getName() + " - is this an unsupported JVM?");
                }
            }
        }

        throw new IllegalArgumentException("Unknown regex flag: " + fieldName);
    }
}
