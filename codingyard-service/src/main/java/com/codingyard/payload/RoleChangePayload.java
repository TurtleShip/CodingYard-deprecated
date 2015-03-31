package com.codingyard.payload;

import com.codingyard.entity.user.Role;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class RoleChangePayload {

    private final Long userId;
    private final Role newRole;

    @JsonCreator
    public RoleChangePayload(@NotNull @JsonProperty("user_id") Long userId,
                             @NotNull @JsonProperty("new_role") Role newRole) {
        this.userId = userId;
        this.newRole = newRole;
    }

    @NotNull
    @JsonProperty("user_id")
    public Long getUserId() {
        return userId;
    }

    @NotNull
    @JsonProperty("new_role")
    public Role getNewRole() {
        return newRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, newRole);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RoleChangePayload other = (RoleChangePayload) obj;
        return Objects.equals(this.userId, other.userId)
            && Objects.equals(this.newRole, other.newRole);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(RoleChangePayload.class)
            .add("user_id", userId)
            .add("new_role", newRole)
            .toString();
    }
}
