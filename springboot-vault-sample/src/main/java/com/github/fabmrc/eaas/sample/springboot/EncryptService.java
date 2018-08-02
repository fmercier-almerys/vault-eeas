package com.github.fabmrc.eaas.sample.springboot;

import com.github.fabmrc.eaas.sample.springboot.config.SpringVaultProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabmrc.eaas.api.EncryptEngine;
import com.github.fabmrc.eaas.api.EnhancedJsonNode;

import java.io.IOException;

public class EncryptService {

    private ObjectMapper objectMapper;

    private SpringVaultProperties vaultProperties;

    private EncryptEngine engine;

    public EncryptService(ObjectMapper objectMapper, SpringVaultProperties vaultProperties, EncryptEngine engine) {
        this.objectMapper = objectMapper;
        this.vaultProperties = vaultProperties;
        this.engine = engine;
    }

    public String encryptRequest(String json) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(json);
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(jsonNode);
        EnhancedJsonNode encryptedNode = engine.encrypt(enhancedJsonNode, enhancedJsonNode.getResolvedPointers(vaultProperties.getFields()));
        return objectMapper.writeValueAsString(encryptedNode.getJsonNode());
    }

    public String decryptRequest(String json) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(json);
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(jsonNode);
        EnhancedJsonNode decryptedNode = engine.decrypt(enhancedJsonNode,  enhancedJsonNode.getResolvedPointers(vaultProperties.getFields()));
        return objectMapper.writeValueAsString(decryptedNode.getJsonNode());
    }
}
