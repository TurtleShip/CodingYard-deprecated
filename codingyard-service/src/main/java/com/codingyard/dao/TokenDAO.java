package com.codingyard.dao;

import com.codingyard.entity.auth.CodingyardToken;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.Optional;

public class TokenDAO extends AbstractDAO<CodingyardToken> {

    public TokenDAO(final SessionFactory factory) {
        super(factory);
    }

    public long save(final CodingyardToken token) {
        return persist(token).getUserId();
    }

    public Optional<CodingyardToken> findByTokenValue(final String value) {
        final CodingyardToken result
            = (CodingyardToken) currentSession().createCriteria(CodingyardToken.class)
            .add(Restrictions.eq("value", value))
            .uniqueResult();

        return Optional.ofNullable(result);
    }
}
