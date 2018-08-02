package com.github.fabmrc.eaas.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabmrc.eaas.api.jackson.DecryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.DecryptVaultResponse;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VaultEncryptEngineTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private VaultDriver driverMock;

    private String json = "{\"items\":[{\"sub_item\":\"value0\"},{\"sub_item\":\"value1\"}]}";

    private String cipheredJson = "{\"items\":[{\"sub_item\":\"cipher0\"},{\"sub_item\":\"cipher1\"}]}";

    private List<String> pointers = Arrays.asList("/items/0/sub_item", "/items/1/sub_item");

    @Test
    public void shouldEncryptMessage() throws IOException {
        EncryptVaultResponse responseMock = mock(EncryptVaultResponse.class);
        JsonNode jsonNode = objectMapper.readTree(json);
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(jsonNode);

        List<String> cipheredValues = Arrays.asList("cipher0", "cipher1");
        when(responseMock.getCipheredValues()).thenReturn(cipheredValues);
        when(driverMock.encrypt(isA(EncryptVaultRequest.class))).thenReturn(responseMock);

        EncryptEngine engine = new VaultEncryptEngine(driverMock);
        EnhancedJsonNode encryptedJsonNode = engine.encrypt(enhancedJsonNode, pointers);

        assertEquals(cipheredJson, objectMapper.writeValueAsString(encryptedJsonNode.getJsonNode()));
    }

    @Test
    public void shouldDecryptMessage() throws IOException {
        DecryptVaultResponse responseMock = mock(DecryptVaultResponse.class);
        JsonNode cipheredJsonNode = objectMapper.readTree(cipheredJson);
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(cipheredJsonNode);

        List<String> values = Arrays.asList("value0", "value1");
        when(responseMock.getValues()).thenReturn(values);
        when(driverMock.decrypt(isA(DecryptVaultRequest.class))).thenReturn(responseMock);

        EncryptEngine engine = new VaultEncryptEngine(driverMock);
        EnhancedJsonNode decryptedJsonNode = engine.decrypt(enhancedJsonNode, pointers);

        assertEquals(json, objectMapper.writeValueAsString(decryptedJsonNode.getJsonNode()));
    }
}
