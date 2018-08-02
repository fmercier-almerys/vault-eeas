package com.github.fabmrc.eaas.api.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DecryptVaultResponseTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Test
    public void shouldDeSerializeDecryptVaultResponse() throws IOException {
        String response = "{\"data\":{\"batch_results\":[{\"plaintext\":\"YQ==\"},{\"plaintext\":\"Yg==\"}]}}";
        DecryptVaultResponse decryptVaultResponse = objectMapper.readValue(response, DecryptVaultResponse.class);

        List<String> values = Arrays.asList("a", "b");
        assertEquals(values, decryptVaultResponse.getValues());
    }
}
