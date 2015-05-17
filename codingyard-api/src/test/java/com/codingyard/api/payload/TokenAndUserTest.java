package com.codingyard.api.payload;

import com.codingyard.api.entity.EntityTestUtil;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertTrue;

public class TokenAndUserTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
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

    @Test
    public void serializesToJSON() throws Exception {
        final String actual = MAPPER.writeValueAsString(payload);
        final String expected = fixture("fixtures/payload/valid_token_and_user.json");
        assertTrue(EntityTestUtil.isSameInJson(actual, expected));
    }

    @Test
    public void deserializeFromJson() throws Exception {
        final TokenAndUser actual = MAPPER.readValue(fixture("fixtures/payload/valid_token_and_user.json"), TokenAndUser.class);
        assertTrue(payload.equals(actual));
    }
}
