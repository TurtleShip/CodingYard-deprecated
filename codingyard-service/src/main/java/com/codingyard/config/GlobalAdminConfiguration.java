package com.codingyard.config;

import javax.validation.constraints.NotNull;

public class GlobalAdminConfiguration {

    @NotNull
    private String username;

    @NotNull
    private String password;

    private String firstName = "admin";

    private String lastName = "admin";

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
