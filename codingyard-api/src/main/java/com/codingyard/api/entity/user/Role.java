package com.codingyard.api.entity.user;

public enum Role {

    GLOBAL_ADMIN("GLOBAL_ADMIN", 3), ADMIN("ADMIN" ,2), MEMBER("MEMBER", 1), GUEST("GUEST", 0);

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

    @Override
    public String toString() {
        return title;
    }

}
