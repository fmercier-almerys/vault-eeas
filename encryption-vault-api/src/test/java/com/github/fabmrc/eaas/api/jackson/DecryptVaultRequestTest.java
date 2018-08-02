package com.github.fabmrc.eaas.api.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabmrc.eaas.api.jackson.DecryptVaultRequest.DecryptBatchInput;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DecryptVaultRequestTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldSerializeDecryptVaultRequest() throws JsonProcessingException {
        List<DecryptBatchInput> batchInput = Arrays.asList(new DecryptBatchInput("YQ=="), new DecryptBatchInput("Yg=="));
        DecryptVaultRequest request = new DecryptVaultRequest(batchInput);

        String expected = "{\"batch_input\":[{\"ciphertext\":\"YQ==\"},{\"ciphertext\":\"Yg==\"}]}";
        assertEquals(expected, objectMapper.writeValueAsString(request));
    }

    @Test
    public void shouldBuildDecryptVaultRequest() throws JsonProcessingException {
        List<String> values = Arrays.asList("YQ==", "Yg==");
        DecryptVaultRequest request = DecryptVaultRequest.build(values);

        String expected = "{\"batch_input\":[{\"ciphertext\":\"YQ==\"},{\"ciphertext\":\"Yg==\"}]}";
        assertEquals(expected, objectMapper.writeValueAsString(request));
    }
}
