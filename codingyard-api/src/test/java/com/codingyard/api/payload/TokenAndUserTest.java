package com.codingyard.api.payload;

import com.codingyard.api.BasicJsonTest;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import org.junit.Before;

public class TokenAndUserTest extends BasicJsonTest<TokenAndUser> {

    private final CodingyardUser user = new CodingyardUser.Builder("TurtleShip", "safe_password")
        .firstName("Seulgi")
        .lastName("Kim")
        .role(Role.GLOBAL_ADMIN)
        .build();
    private final String token = "very-secure-token";
    final TokenAndUser payload = new TokenAndUser(token, user);

    @Before
    public void setup() {
        user.setId(1L);
    }

    @Override
    public String getJsonFilePath() {
        return "fixtures/payload/valid_token_and_user.json";
    }

    @Override
    public TokenAndUser getValidPojo() {
        return payload;
    }

    @Override
    public Class<? extends TokenAndUser> getEntityClass() {
        return TokenAndUser.class;
    }
}
