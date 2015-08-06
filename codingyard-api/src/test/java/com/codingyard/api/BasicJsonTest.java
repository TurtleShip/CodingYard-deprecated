package com.codingyard.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.io.IOException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertTrue;

public abstract class BasicJsonTest<Pojo> {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    public abstract String getJsonFilePath();

    public abstract Pojo getValidPojo();

    public abstract Class<? extends Pojo> getEntityClass();

    @Test
    public void serializeToJson() throws Exception {
        final String actual = MAPPER.writeValueAsString(getValidPojo());
        final String expected = fixture(getJsonFilePath());
        System.out.println(actual);
        System.out.println(expected);
        assertTrue(isSameInJson(actual, expected));
    }

    @Test
    public void deserializeFromJson() throws Exception {
        final Pojo actual = MAPPER.readValue(fixture(getJsonFilePath()), getEntityClass());
        assertTrue(getValidPojo().equals(actual));
    }

    public boolean isSameInJson(final String actual,
                                final String expected) throws IOException {
        JsonNode act = toJsonNode(actual);
        JsonNode exp = toJsonNode(expected);
        return toJsonNode(actual).equals(toJsonNode(expected));
    }

    public JsonNode toJsonNode(final String data) throws IOException {
        return MAPPER.readValue(data, JsonNode.class);
    }
}
