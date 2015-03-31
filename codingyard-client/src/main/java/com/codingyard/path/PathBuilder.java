package com.codingyard.path;

public class PathBuilder {

    private static final String PATH_SEPARATOR = "/";

    public static String build(final String root, final String... path) {
        StringBuilder builder = new StringBuilder(root);
        for(final String current : path) {
            builder.append(PATH_SEPARATOR).append(current);
        }
        return builder.toString();
    }
}
