package com.codingyard.dao;

import com.codingyard.api.entity.user.CodingyardUser;
import com.google.common.base.Optional;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


public class UserDAO extends BasicDAO<CodingyardUser> {

    public UserDAO(final SessionFactory factory) {
        super(factory, CodingyardUser.class);
    }

    public Optional<CodingyardUser> findByUsername(final String username) {
        final CodingyardUser result = (CodingyardUser) currentSession().createCriteria(CodingyardUser.class)
            .add(Restrictions.eq("username", username))
            .uniqueResult();

        return Optional.fromNullable(result);
    }
}
