package com.codingyard.dao;

import com.codingyard.api.entity.auth.CodingyardToken;
import com.google.common.base.Optional;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class TokenDAO extends BasicDAO<CodingyardToken> {

    public TokenDAO(final SessionFactory factory) {
        super(factory, CodingyardToken.class);
    }

    public Optional<CodingyardToken> findByTokenValue(final String value) {
        final CodingyardToken result
            = (CodingyardToken) currentSession().createCriteria(CodingyardToken.class)
            .add(Restrictions.eq("value", value))
            .uniqueResult();

        return Optional.fromNullable(result);
    }
}
