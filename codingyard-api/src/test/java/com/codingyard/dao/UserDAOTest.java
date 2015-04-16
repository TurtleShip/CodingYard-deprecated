package com.codingyard.dao;

import com.codingyard.api.entity.user.CodingyardUser;
import com.google.common.base.Optional;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class UserDAOTest extends H2Test {

    private final UserDAO userDAO = new UserDAO(sessionFactory);

    @Test
    public void createAndSaveUser() {
        CodingyardUser user = new CodingyardUser.Builder("TurtleShip", "secure_password").build();
        Long id = userDAO.save(user);

        Optional<CodingyardUser> searchResult = userDAO.findById(id);
        assertTrue(searchResult.isPresent());

        assertThat(searchResult.get()).isEqualTo(user);
    }

}
