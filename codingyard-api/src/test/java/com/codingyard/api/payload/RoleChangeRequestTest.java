package com.codingyard.api.payload;

import com.codingyard.api.entity.EntityTestUtil;
import com.codingyard.api.entity.user.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertTrue;

public class RoleChangeRequestTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private final RoleChangeRequest payload = new RoleChangeRequest(1L, Role.ADMIN);

    @Test
    public void serializesToJSON() throws Exception {
        final String actual = MAPPER.writeValueAsString(payload);
        final String expected = fixture("fixtures/payload/role_change.json");
        assertTrue(EntityTestUtil.isSameInJson(actual, expected));
    }

    @Test
    public void deserializeFromJson() throws Exception {
        final RoleChangeRequest actual = MAPPER.readValue(fixture("fixtures/payload/role_change.json"), RoleChangeRequest.class);
        assertTrue(payload.equals(actual));
    }
}
