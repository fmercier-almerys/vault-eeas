package com.github.fabmrc.eaas.api;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EnhancedJsonNode {

    private JsonNode jsonNode;

    private final String ARRAY_WILDCARD = "/*";

    public EnhancedJsonNode(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    public List<String> getResolvedPointers(List<String> rawPointers) {
        List<String> wildcardFields = rawPointers.stream().filter(f -> f.contains(ARRAY_WILDCARD)).map(f -> expandAllWilcards(f)).flatMap(l -> l.stream()).collect(Collectors.toList());
        List<String> simpleFields = rawPointers.stream().filter(f -> !f.contains("/*")).collect(Collectors.toList());
        List<String> resolvedPointers = new ArrayList<>();
        resolvedPointers.addAll(simpleFields);
        resolvedPointers.addAll(wildcardFields);
        return resolvedPointers;
    }

    public List<String> getValues(List<String> pointers) {
        return pointers.stream().map(this::getValue).collect(Collectors.toList());
    }

    public String getValue(String pointer) {
        return jsonNode.at(pointer).asText();
    }

    public JsonNode getJsonNode() {
        return jsonNode;
    }

    public void replaceValues(List<String> pointers, List<String> values) {
        for (int i = 0; i < pointers.size(); i++) {
            String field = pointers.get(i);
            replaceValue(field, values.get(i));
        }
    }

    public void replaceValue(String pointer, String value) {
        JsonPointer jsonPointer = JsonPointer.compile(pointer);
        JsonNode parentNode = jsonNode.at(jsonPointer.head());
        String leaf = jsonPointer.last().toString().substring(1);
        if (parentNode.isArray()) {
            int index = Integer.parseInt(leaf);
            ((ArrayNode) parentNode).set(index, new TextNode(value));
        } else {
            ((ObjectNode) parentNode).put(leaf, value);
        }
    }

    private List<String> expandAllWilcards(String pointer) {
        List<String> expandedFields = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        stack.push(pointer);
        while (!stack.isEmpty()) {
            String tmp = stack.pop();
            List<String> fields = expandFirstWildcard(tmp);
            for (String field : fields) {
                if (field.contains(ARRAY_WILDCARD)) {
                    stack.push(field);
                } else {
                    expandedFields.add(field);
                }
            }
        }
        return expandedFields;
    }

    private List<String> expandFirstWildcard(String pointer) {
        int i = pointer.indexOf(ARRAY_WILDCARD);
        String head = pointer.substring(0, i);
        List<String> expandedFields = expandJsonPointer(head);
        String leaf = pointer.substring(i + 2, pointer.length());
        expandedFields = expandedFields.stream().map(f -> f.concat(leaf)).collect(Collectors.toList());
        return expandedFields;
    }

    private List<String> expandJsonPointer(String pointer) {
        JsonPointer jsonPointer = JsonPointer.compile(pointer);
        ArrayNode node = (ArrayNode) jsonNode.at(jsonPointer);
        return IntStream.range(0, node.size()).mapToObj(n -> String.format(pointer + "/%s", n)).collect(Collectors.toList());
    }
}
