package com.github.fabmrc.eaas.api.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultRequest.EncryptBatchInput;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EncryptVaultRequestTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldSerializeEncryptVaultRequest() throws JsonProcessingException {
        List<EncryptBatchInput> batchInput = Arrays.asList(new EncryptBatchInput("a"), new EncryptBatchInput("b"));
        EncryptVaultRequest request = new EncryptVaultRequest(batchInput);

        String expected = "{\"batch_input\":[{\"plaintext\":\"YQ==\"},{\"plaintext\":\"Yg==\"}]}";
        assertEquals(expected, objectMapper.writeValueAsString(request));
    }

    @Test
    public void shouldBuildEncryptVaultRequest() throws JsonProcessingException {
        List<String> values = Arrays.asList("a", "b");
        EncryptVaultRequest request = EncryptVaultRequest.build(values);

        String expected = "{\"batch_input\":[{\"plaintext\":\"YQ==\"},{\"plaintext\":\"Yg==\"}]}";
        assertEquals(expected, objectMapper.writeValueAsString(request));
    }
}
