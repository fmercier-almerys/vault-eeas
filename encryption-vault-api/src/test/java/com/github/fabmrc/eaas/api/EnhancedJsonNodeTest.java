package com.github.fabmrc.eaas.api;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnhancedJsonNodeTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldGetCorrectValue() throws IOException {
        String json = "{\"item\":{\"sub_item\":\"value\"}}";
        JsonNode jsonNode = objectMapper.readTree(json);
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(jsonNode);

        assertEquals("value", enhancedJsonNode.getValue("/item/sub_item"));
    }

    @Test
    public void shouldGetCorrectValues() throws IOException {
        String json = "{\"items\":[{\"sub_item\":\"a\"},{\"sub_item\":\"b\"}]}";
        List<String> values = Arrays.asList("a", "b");
        List<String> pointers = Arrays.asList("/items/0/sub_item", "/items/1/sub_item");
        JsonNode jsonNode = objectMapper.readTree(json);
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(jsonNode);

        assertEquals(values, enhancedJsonNode.getValues(pointers));
    }

    @Test
    public void shouldReplaceValueInArray() throws IOException {
        String json = "{\"items\":[\"1\",\"2\",\"3\"]}";
        String newValue = "0";
        JsonPointer pointer = JsonPointer.compile("/items/0");
        JsonNode jsonNode = objectMapper.readTree(json);
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(jsonNode);
        enhancedJsonNode.replaceValue(pointer.toString(), newValue);

        assertEquals(newValue, jsonNode.at(pointer).asText());
    }

    @Test
    public void shouldReplaceAllValuesInArray() throws IOException {
        String json = "{\"items\":[\"1\",\"2\",\"3\"]}";
        List<String> newValues = Arrays.asList("a", "b", "c");
        List<String> pointers = Arrays.asList("/items/0", "/items/1", "/items/2");
        JsonNode jsonNode = objectMapper.readTree(json);
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(jsonNode);
        enhancedJsonNode.replaceValues(pointers, newValues);

        assertEquals("{\"items\":[\"a\",\"b\",\"c\"]}", objectMapper.writeValueAsString(jsonNode));
    }

    @Test
    public void shouldReplaceValueInObject() throws IOException {
        String json = "{\"item\":{\"sub_item\":\"value\"}}";
        String newValue = "new_value";
        JsonPointer pointer = JsonPointer.compile("/item/sub_item");
        JsonNode jsonNode = objectMapper.readTree(json);
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(jsonNode);
        enhancedJsonNode.replaceValue(pointer.toString(), newValue);

        assertEquals(newValue, jsonNode.at(pointer).asText());
    }

    @Test
    public void shouldReplaceAllValueObject() throws IOException {
        String json = "{\"items\":[{\"sub_item\":\"value0\"},{\"sub_item\":\"value1\"}]}";
        List<String> newValues = Arrays.asList("a", "b");
        List<String> pointers = Arrays.asList("/items/0/sub_item", "/items/1/sub_item");
        JsonNode jsonNode = objectMapper.readTree(json);
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(jsonNode);
        enhancedJsonNode.replaceValues(pointers, newValues);

        assertEquals("{\"items\":[{\"sub_item\":\"a\"},{\"sub_item\":\"b\"}]}", objectMapper.writeValueAsString(jsonNode));
    }

    @Test
    public void shouldExpandAllJsonNode() throws IOException {
        String json = "{\"items\":[{\"raw\":[{\"raw\":\"toto\"},{\"raw\":\"titi\"}]},{\"raw\":[{\"raw\":\"toto\"},{\"raw\":\"titi\"}]}]}";
        JsonNode jsonNode = objectMapper.readTree(json);
        List<String> pointers = Arrays.asList("/items/*/raw/*/raw");
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(jsonNode);
        List<String> expandedPointers = enhancedJsonNode.getResolvedPointers(pointers);

        assertEquals(4, expandedPointers.size());
        assertTrue(expandedPointers.contains("/items/0/raw/0/raw"));
        assertTrue(expandedPointers.contains("/items/0/raw/1/raw"));
        assertTrue(expandedPointers.contains("/items/1/raw/1/raw"));
        assertTrue(expandedPointers.contains("/items/1/raw/1/raw"));
    }

    @Test
    public void shouldExpandAllValueForSimpleArray() throws IOException {
        String json = "{\"items\":[\"0\",\"0\",\"0\"]}";
        JsonNode jsonNode = objectMapper.readTree(json);
        List<String> pointers = Arrays.asList("/items/*");
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(jsonNode);
        List<String> expandedPointers = enhancedJsonNode.getResolvedPointers(pointers);

        assertEquals(3, expandedPointers.size());
        assertTrue(expandedPointers.contains("/items/0"));
        assertTrue(expandedPointers.contains("/items/1"));
        assertTrue(expandedPointers.contains("/items/2"));
    }

}
