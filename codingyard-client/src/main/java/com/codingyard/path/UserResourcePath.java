package com.codingyard.path;

public class UserResourcePath {

    public static final String ROOT = "user";
    public static final String CREATE_USER_PATH = ROOT;
    public static final String LOGIN_PATH = PathBuilder.build(ROOT, "login");
    public static final String CHANGE_ROLE_PATH = PathBuilder.build(ROOT, "role");

    public static String findUserPath(final Long userId) {
        return PathBuilder.build(ROOT, Long.toString(userId));
    }

    public static String findSolutionsPath(final Long userId) {
        return PathBuilder.build(ROOT, Long.toString(userId), "solutions");
    }

}
