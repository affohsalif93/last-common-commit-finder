package org.assignment.utils;

public class StringUtils {

    public static String stripIfExist(String str) {
        return str == null ? null : str.strip();
    }

    public static boolean hasText(String str) {
        return str != null && !str.isEmpty() && !str.isBlank();
    }
}
