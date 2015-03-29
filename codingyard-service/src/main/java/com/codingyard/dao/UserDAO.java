package com.codingyard.dao;

import com.codingyard.entity.auth.CodingyardToken;
import com.codingyard.entity.user.CodingyardUser;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;


public class UserDAO extends AbstractDAO<CodingyardUser> {

    public UserDAO(final SessionFactory factory) {
        super(factory);
    }

    public Optional<CodingyardUser> findById(final Long id) {
        return Optional.fromNullable(get(id));
    }

    public Optional<CodingyardUser> findByUsername(final String username) {
        final CodingyardUser result = (CodingyardUser) currentSession().createCriteria(CodingyardUser.class)
            .add(Restrictions.eq("username", username))
            .uniqueResult();

        return Optional.fromNullable(result);
    }

    public long save(final CodingyardUser codingyardUser) {
        return persist(codingyardUser).getUserId();
    }

    public CodingyardToken refreshToken(final CodingyardUser user) {
        final CodingyardToken newToken = CodingyardToken.Builder.build();
        user.getToken().setCreatedAt(newToken.getCreatedAt());
        user.getToken().setValue(newToken.getValue());
        persist(user);
        return user.getToken();
    }

    public List<CodingyardUser> findAll() {
        return list(currentSession().createCriteria(CodingyardUser.class));
    }
}
