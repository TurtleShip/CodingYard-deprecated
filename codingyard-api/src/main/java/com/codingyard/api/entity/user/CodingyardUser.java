package com.codingyard.api.entity.user;

import com.codingyard.api.entity.auth.CodingyardToken;
import com.codingyard.api.entity.contest.Solution;
import com.codingyard.api.util.Encryptor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

// Note that table name cannot be 'user' since it is reserved table name for postgresql
@Entity
@Table(name = "codingyard_user")
public class CodingyardUser {

    private Long userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
    private CodingyardToken token;
    private Set<Solution> solutions = Sets.newHashSet();

    // package private. Needed for hibernate
    CodingyardUser() {
    }

    private CodingyardUser(String username, String password, String firstName, String lastName, Role role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    @JsonProperty("id")
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getUserId() {
        return userId;
    }

    @JsonProperty("username")
    @Column(name = "user_name", nullable = false, unique = true)
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    @JsonProperty("first_name")
    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("last_name")
    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("role")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false)
    public Role getRole() {
        return role;
    }

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, optional = false, mappedBy = "user", cascade = CascadeType.ALL)
    public CodingyardToken getToken() {
        return token;
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author", cascade = CascadeType.ALL)
    public Set<Solution> getSolutions() {
        return solutions;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setToken(CodingyardToken token) {
        this.token = token;
    }

    public void setSolutions(Set<Solution> solutions) {
        this.solutions = solutions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, role);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CodingyardUser other = (CodingyardUser) obj;
        return Objects.equals(this.username, other.username)
            && Objects.equals(this.firstName, other.firstName)
            && Objects.equals(this.lastName, other.lastName)
            && Objects.equals(this.role, other.role);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(CodingyardUser.class)
            .add("userId", userId)
            .add("userName", username)
            .add("firstName", firstName)
            .add("lastName", lastName)
            .add("role", role)
            .toString();
    }

    public static class Builder {
        private static final String DEFAULT_FIRSTNAME = "default";
        private static final String DEFAULT_LASTNAME = "default";
        private static final Role DEFAULT_ROLE = Role.GUEST;

        private final String username;
        private final String password;
        private String firstName = DEFAULT_FIRSTNAME;
        private String lastName = DEFAULT_LASTNAME;
        private Role role = DEFAULT_ROLE;

        /**
         * Builder for CodingyardUser<br/>
         * The below are default values<br/>
         * firstName : "default"<br/>
         * lastName  : "default"<br/>
         * role      : Role.GUEST
         *
         * @param username username of this user.
         * @param password password of this user. Note that password will be encrypted using {@code Encryptor.encrypt}
         */
        public Builder(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public CodingyardUser build() {
            final CodingyardUser user = new CodingyardUser(username, Encryptor.encrypt(password), firstName, lastName, role);
            final CodingyardToken token = CodingyardToken.Builder.build();
            user.setToken(token);
            token.setUser(user);
            return user;
        }
    }
}