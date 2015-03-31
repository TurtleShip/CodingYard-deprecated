package com.codingyard.api.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;

import java.io.IOException;

public class EntityTestUtil {


    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    public static boolean isSameInJson(final String actual,
                                       final String expected) throws IOException {
        return toJsonNode(actual).equals(toJsonNode(expected));
    }

    public static JsonNode toJsonNode(final String data) throws IOException {
        return MAPPER.readValue(data, JsonNode.class);
    }

}
