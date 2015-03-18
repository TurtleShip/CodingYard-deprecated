package com.codingyard.api.user;

public enum Role {

    GLOBAL_ADMIN("Global admin", 3), ADMIN("Admin" ,2), MEMBER("Member", 1), GUEST("Guest", 0);

    private final String title;
    private final int level;

    private Role(final String title, final int level) {
        this.title = title;
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public int getLevel() {
        return level;
    }
}
