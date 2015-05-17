package com.codingyard.api.entity.user;

import com.codingyard.api.entity.EntityTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertTrue;


public class CodingyardUserTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    final CodingyardUser user = new CodingyardUser.Builder("TurtleShip", "safe_password")
        .firstName("Seulgi")
        .lastName("Kim")
        .role(Role.GLOBAL_ADMIN)
        .build();

    @Before
    public void setUp() {
        user.setId(1L);
    }

    @Test
    public void serializesToJSON() throws Exception {
        final String actual = MAPPER.writeValueAsString(user);
        final String expected = fixture("fixtures/user/valid_user.json");
        assertTrue(EntityTestUtil.isSameInJson(actual, expected));
    }

    @Test
    public void deserializeFromJson() throws Exception {
        final CodingyardUser actual = MAPPER.readValue(fixture("fixtures/user/valid_user.json"), CodingyardUser.class);
        assertTrue(user.equals(actual));
    }

}
