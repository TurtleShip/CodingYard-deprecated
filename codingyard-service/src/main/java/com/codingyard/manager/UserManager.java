package com.codingyard.manager;

import com.codingyard.api.entity.auth.CodingyardToken;
import com.codingyard.api.entity.contest.Solution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.dao.UserDAO;
import com.google.common.base.Optional;

import java.util.List;

/**
 * This class is responsible for creating / retrieving / updating user information.
 */
public class UserManager {

    private final UserDAO userDAO;

    public UserManager(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Optional<CodingyardUser> findById(final Long id) {
        return userDAO.findById(id);
    }

    public Optional<CodingyardUser> findByUsername(final String username) {
        return userDAO.findByUsername(username);
    }

    public long save(final CodingyardUser codingyardUser) {
        return userDAO.save(codingyardUser);
    }

    public CodingyardToken refreshToken(final CodingyardUser user) {
        final CodingyardToken newToken = CodingyardToken.Builder.build();
        user.getToken().setCreatedAt(newToken.getCreatedAt());
        user.getToken().setValue(newToken.getValue());
        userDAO.save(user);
        return user.getToken();
    }

    public List<CodingyardUser> getAllUsers() {
        return userDAO.findAll();
    }

    public void saveSolution(final CodingyardUser author, final Solution solution) {
        author.getSolutions().add(solution);
        solution.setAuthor(author);
        userDAO.save(author);
    }


}
