package com.codingyard.manager;

import com.codingyard.api.entity.BasicEntity;
import com.codingyard.dao.BasicDAO;
import com.google.common.base.Optional;

import java.util.List;

public class BasicEntityManager<E extends BasicEntity> {

    private final BasicDAO<E> basicDAO;

    public BasicEntityManager(final BasicDAO<E> basicDAO) {
        this.basicDAO = basicDAO;
    }

    public long save(final E entity) {
        return basicDAO.save(entity);
    }

    public void flush() {
        basicDAO.flush();
    }

    public Optional<E> findById(final Long id) {
        return basicDAO.findById(id);
    }

    public List<E> findAll() {
        return basicDAO.findAll();
    }

    public boolean delete(final E entity) {
        return basicDAO.delete(entity);
    }

    public boolean deleteById(final Long id) {
        return basicDAO.deleteById(id);
    }
}
