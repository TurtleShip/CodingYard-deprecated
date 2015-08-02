package com.codingyard.dao;

import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.uva.UVaSolution;
import com.google.common.base.Optional;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class UVaSolutionDAO extends BasicDAO<UVaSolution> {

    public UVaSolutionDAO(final SessionFactory sessionFactory) {
        super(sessionFactory, UVaSolution.class);
    }

    public List<UVaSolution> findAll(Optional<Long> problemNumber,
                                     Optional<Language> language,
                                     Optional<Long> userId) {
        Criteria criteria = currentSession().createCriteria(UVaSolution.class);

        if (problemNumber.isPresent()) criteria.add(Restrictions.eq("problemNumber", problemNumber.get()));
        if (language.isPresent()) criteria.add(Restrictions.eq("language", language.get()));
        if (userId.isPresent()) criteria.add(Restrictions.eq("author.userId", userId.get()));

        return criteria.list();
    }
}
