package com.codingyard.dao;

import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.api.entity.contest.topcoder.TopCoderDivision;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

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

    public List<TopCoderSolution> findAll(Optional<TopCoderDivision> division,
                                          Optional<TopCoderDifficulty> difficulty,
                                          Optional<Long> problemNumber,
                                          Optional<Language> language,
                                          Optional<Long> userId) {
        Criteria criteria = currentSession().createCriteria(TopCoderSolution.class);

        if (division.isPresent()) criteria.add(Restrictions.eq("division", division.get()));
        if (difficulty.isPresent()) criteria.add(Restrictions.eq("difficulty", difficulty.get()));
        if (problemNumber.isPresent()) criteria.add(Restrictions.eq("problemNumber", problemNumber.get()));
        if (language.isPresent()) criteria.add(Restrictions.eq("language", language.get()));
        if (userId.isPresent()) criteria.add(Restrictions.eq("author.userId", userId.get()));

        return criteria.list();
    }
}
