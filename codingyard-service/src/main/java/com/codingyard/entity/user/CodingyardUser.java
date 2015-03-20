package com.codingyard.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

// Note that table name cannot be 'user' since it is reserved table name for postgresql
@Entity
@Table(name = "codingyard_user")
public class CodingyardUser {

    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;

    // package private. Needed for hibernate
    CodingyardUser() {
    }

    public CodingyardUser(String username, String password, String firstName, String lastName, Role role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    @JsonIgnore
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    @JsonProperty("username")
    @Column(name = "user_name", nullable = false)
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
    @Column(name = "wassup", nullable = false)
    public Role getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
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
}
