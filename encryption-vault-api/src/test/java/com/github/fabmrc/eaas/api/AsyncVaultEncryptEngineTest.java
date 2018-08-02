package com.github.fabmrc.eaas.api;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncVaultEncryptEngineTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AsyncVaultDriver driverMock;

    private String json = "{\"items\":[{\"sub_item\":\"value0\"},{\"sub_item\":\"value1\"}]}";

    private String cipheredJson = "{\"items\":[{\"sub_item\":\"cipher0\"},{\"sub_item\":\"cipher1\"}]}";

    private List<String> pointers = Arrays.asList("/items/0/sub_item", "/items/1/sub_item");

    @Test
    public void shouldEncryptMessage() throws Exception {
        EncryptVaultResponse responseMock = mock(EncryptVaultResponse.class);
        JsonNode jsonNode = objectMapper.readTree(json);
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(jsonNode);

        List<String> cipheredValues = Arrays.asList("cipher0", "cipher1");
        when(responseMock.getCipheredValues()).thenReturn(cipheredValues);

        AsyncEncryptEngine engine = new AsyncVaultEngine(driverMock);

        doAnswer(invocation -> {
            Consumer<EncryptVaultResponse> onResponse = invocation.getArgumentAt(1, Consumer.class);
            onResponse.accept(responseMock);
            return null;
        }).when(driverMock).encrypt(isA(EncryptVaultRequest.class), isA(Consumer.class),  isA(Consumer.class));

        engine.encrypt(enhancedJsonNode, pointers, encryptVaultResponse -> {
            try {
                assertEquals(cipheredJson, objectMapper.writeValueAsString(enhancedJsonNode.getJsonNode()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }, e -> {});
    }

    @Test
    public void shouldDecryptMessage() throws Exception {
        DecryptVaultResponse responseMock = mock(DecryptVaultResponse.class);
        JsonNode cipheredJsonNode = objectMapper.readTree(cipheredJson);
        EnhancedJsonNode enhancedJsonNode = new EnhancedJsonNode(cipheredJsonNode);

        List<String> values = Arrays.asList("value0", "value1");
        when(responseMock.getValues()).thenReturn(values);
        AsyncEncryptEngine engine = new AsyncVaultEngine(driverMock);

        doAnswer(invocation -> {
            Consumer<DecryptVaultResponse> onResponse = invocation.getArgumentAt(1, Consumer.class);
            onResponse.accept(responseMock);
            return null;
        }).when(driverMock).decrypt(isA(DecryptVaultRequest.class), isA(Consumer.class),  isA(Consumer.class));

        engine.decrypt(enhancedJsonNode, pointers, decryptVaultResponse -> {
            try {
                assertEquals(json, objectMapper.writeValueAsString(enhancedJsonNode.getJsonNode()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }, e -> {});
    }

}
