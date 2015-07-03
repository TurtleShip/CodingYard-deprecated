package com.codingyard.api.entity.user;

import com.codingyard.api.BasicJsonTest;
import org.junit.Before;

public class CodingyardUserTest extends BasicJsonTest<CodingyardUser> {

    final CodingyardUser user = new CodingyardUser.Builder("TurtleShip", "safe_password")
        .firstName("Seulgi")
        .lastName("Kim")
        .role(Role.GLOBAL_ADMIN)
        .email("test@codingyard.com")
        .build();

    @Before
    public void setUp() {
        user.setId(1L);
    }

    @Override
    public String getJsonFilePath() {
        return "fixtures/user/valid_user.json";
    }

    @Override
    public CodingyardUser getValidPojo() {
        return user;
    }

    @Override
    public Class<? extends CodingyardUser> getEntityClass() {
        return CodingyardUser.class;
    }
}
