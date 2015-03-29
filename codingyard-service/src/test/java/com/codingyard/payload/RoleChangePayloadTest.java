package com.codingyard.payload;

import com.codingyard.entity.EntityTestUtil;
import com.codingyard.entity.user.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertTrue;

public class RoleChangePayloadTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private final RoleChangePayload payload = new RoleChangePayload(1L, Role.ADMIN);

    @Test
    public void serializesToJSON() throws Exception {
        final String actual = MAPPER.writeValueAsString(payload);
        final String expected = fixture("fixtures/payload/role_change.json");
        assertTrue(EntityTestUtil.isSameInJson(actual, expected));
    }

    @Test
    public void deserializeFromJson() throws Exception {
        final RoleChangePayload actual = MAPPER.readValue(fixture("fixtures/payload/role_change.json"), RoleChangePayload.class);
        assertTrue(payload.equals(actual));
    }
}
