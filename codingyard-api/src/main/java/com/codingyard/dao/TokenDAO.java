package com.codingyard.dao;

import com.codingyard.api.entity.auth.CodingyardToken;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

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

        return Optional.fromNullable(result);
    }
}
