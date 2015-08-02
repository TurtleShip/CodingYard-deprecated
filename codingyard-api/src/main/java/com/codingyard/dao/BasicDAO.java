package com.codingyard.dao;

import com.codingyard.api.entity.BasicEntity;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;

public class BasicDAO<E extends BasicEntity> extends AbstractDAO<E> {

    private static final Logger LOG = LoggerFactory.getLogger(BasicDAO.class);
    private final Class entityClass;

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public BasicDAO(final SessionFactory sessionFactory,
                    final Class entityClass) {
        super(sessionFactory);
        this.entityClass = entityClass;
    }

    public long save(final E entity) {
        return persist(entity).getId();
    }

    public void flush() {
        currentSession().flush();
    }

    public Optional<E> findById(final Long id) {
        return Optional.fromNullable(get(id));
    }

    /*
        When fetch type is Eager, list(*) through hibernate will do Join, resulting in
        multiple objects being returned multiple times depending on the number of children they have.
        The concept is explained here :
            http://stackoverflow.com/questions/1995080/hibernate-criteria-returns-children-multiple-times-with-fetchtype-eager

        Ideally, fetch type should be Lazy to be performant, but hibernate wouldn't fetch all fields
         properly for serializing data when in FetchType.Lazy, resulting in
         json representation of objects missing fields.
     */
    public Collection<E> findAll() {
        return new HashSet<>(list(currentSession().createCriteria(entityClass)));
    }

    /**
     * @param entity entity to delete
     * @return {@code true} on successful deletion. {@code false} otherwise.
     */
    public boolean delete(E entity) {
        try {
            currentSession().delete(entity);
            return true;
        } catch (Exception e) {
            LOG.warn("Failed to delete {}", entity, e);
            return false;
        }

    }

    /**
     * @param id The id of an entity to delete
     * @return {@code true} on successful deletion. {@code false} otherwise.
     */
    public boolean deleteById(Long id) {
        Optional<E> entity = findById(id);
        if (entity.isPresent()) {
            return delete(entity.get());
        } else {
            LOG.info("Couldn't find an entity with id {} for class {}", id, entityClass);
            return false;
        }
    }
}
