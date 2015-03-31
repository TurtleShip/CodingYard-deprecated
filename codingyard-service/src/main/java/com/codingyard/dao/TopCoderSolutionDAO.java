package com.codingyard.dao;

import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class TopCoderSolutionDAO extends AbstractDAO<TopCoderSolution> {

    public TopCoderSolutionDAO(final SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public long save(final TopCoderSolution solution) {
        return persist(solution).getSolutionId();
    }

    public Optional<TopCoderSolution> findById(final Long id) {
        return Optional.fromNullable(get(id));
    }
}
