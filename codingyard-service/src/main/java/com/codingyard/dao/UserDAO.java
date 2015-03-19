package com.codingyard.dao;

import com.codingyard.entity.user.CodingyardUser;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDAO extends AbstractDAO<CodingyardUser> {

    public UserDAO(final SessionFactory factory) {
        super(factory);
    }

    public CodingyardUser findById(final Long id) {
        return get(id);
    }

    public long save(final CodingyardUser codingyardUser) {
        return persist(codingyardUser).getId();
    }

    public List<CodingyardUser> findAll() {
        return list(currentSession().createCriteria(CodingyardUser.class));
    }
}
