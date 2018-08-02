package com.github.fabmrc.eaas.api.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EncryptVaultResponseTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Test
    public void shouldDeSerializeEncryptVaultResponse() throws IOException {
        String response = "{\"data\":{\"batch_results\":[{\"ciphertext\":\"YQ==\"},{\"ciphertext\":\"Yg==\"}]}}";
        EncryptVaultResponse encryptVaultResponse = objectMapper.readValue(response, EncryptVaultResponse.class);

        List<String> values = Arrays.asList("YQ==", "Yg==");
        assertEquals(values, encryptVaultResponse.getCipheredValues());
    }
}
