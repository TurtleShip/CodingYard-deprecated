package com.codingyard.api.payload;

import com.codingyard.api.entity.user.CodingyardUser;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * An object that wraps a user and the token of the user.
 * A backend server returns an instance of {@code TokenAndUser} when a user logs in.
 */
public class TokenAndUser {

    private final String token;
    private final CodingyardUser user;

    @JsonCreator
    public TokenAndUser(@NotNull @JsonProperty("token") String token,
                        @NotNull @JsonProperty("user") CodingyardUser user) {
        this.token = token;
        this.user = user;
    }

    @NotNull
    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @NotNull
    @JsonProperty("user")
    public CodingyardUser getUser() {
        return user;
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final TokenAndUser other = (TokenAndUser) obj;
        return Objects.equals(this.token, other.token)
            && Objects.equals(this.user, other.user);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("token", token)
            .add("user", user)
            .toString();
    }
}
