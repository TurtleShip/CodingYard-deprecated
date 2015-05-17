package com.codingyard.api.entity.auth;

import com.codingyard.api.entity.BasicEntity;
import com.codingyard.api.entity.user.CodingyardUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "codingyard_token")
public class CodingyardToken implements BasicEntity {

    private Long id;
    private CodingyardUser user;
    private String value;
    private Date createdAt;

    // Private package. Needed for Hibernate
    CodingyardToken() {
    }

    public CodingyardToken(final String value, final Date createdAt) {
        this.value = value;
        this.createdAt = createdAt;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(generator = "codingyardUserIdGenerator")
    @GenericGenerator(name = "codingyardUserIdGenerator",
        strategy = "foreign",
        parameters = {@Parameter(value = "user", name = "property")})
    public Long getId() {
        return id;
    }

    @OneToOne
    @PrimaryKeyJoinColumn(name = "id")
    public CodingyardUser getUser() {
        return user;
    }

    @Column(name = "value", nullable = false, unique = true)
    public String getValue() {
        return value;
    }

    @Column(name = "created_at", nullable = false)
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(CodingyardUser user) {
        this.user = user;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, createdAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CodingyardToken other = (CodingyardToken) obj;
        return Objects.equals(this.value, other.value)
            && Objects.equals(this.createdAt, other.createdAt);
    }

    public static class Builder {

        public static CodingyardToken build() {
            final Date currentTime = new Date();
            final String token = UUID.randomUUID().toString() + currentTime.getTime();
            return new CodingyardToken(token, currentTime);
        }
    }
}
