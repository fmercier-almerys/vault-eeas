package com.github.fabmrc.eaas.driver.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabmrc.eaas.api.AsyncVaultDriver;
import com.github.fabmrc.eaas.api.VaultProperties;
import com.github.fabmrc.eaas.api.jackson.DecryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.DecryptVaultResponse;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultResponse;
import io.vertx.core.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Consumer;

public class VertxVaultDriver implements AsyncVaultDriver {

    private final static Logger logger = LoggerFactory.getLogger(VertxVaultDriver.class);

    private static final String X_VAULT_TOKEN = "X-Vault-Token";

    private ObjectMapper objectMapper;

    private HttpClient httpClient;

    private VaultProperties properties;

    public VertxVaultDriver(ObjectMapper objectMapper, HttpClient httpClient, VaultProperties properties) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    @Override
    public void encrypt(EncryptVaultRequest request, Consumer<EncryptVaultResponse> onResponse, Consumer<Exception> onError) throws Exception {
        request(request, onResponse, onError, new TypeReference<EncryptVaultResponse>() {});
    }

    @Override
    public void decrypt(DecryptVaultRequest request, Consumer<DecryptVaultResponse> onResponse, Consumer<Exception> onError) throws Exception {
        request(request, onResponse, onError, new TypeReference<DecryptVaultResponse>() {});
    }

    private <T, S> void request(T request, Consumer<S> onResponse, Consumer<Exception> onError, TypeReference<S> typeReference) throws JsonProcessingException {
        String requestStr = objectMapper.writeValueAsString(request);
        httpClient.post(String.join("/", properties.getEncryptionUrl(), properties.getKey()), clientResponse -> clientResponse.bodyHandler(totalBuffer -> {
            String responseBuffer = totalBuffer.getString(0, totalBuffer.length());
            logger.debug("response :" + clientResponse.statusCode() + "; " + responseBuffer);
            if (clientResponse.statusCode() == 200) {
                S vaultResponse;
                try {
                    vaultResponse = objectMapper.readValue(responseBuffer, typeReference);
                    onResponse.accept(vaultResponse);
                } catch (IOException e) {
                    onError.accept(e);
                }
            } else {
                onError.accept(new IllegalStateException("Client response status code is ko :" + clientResponse.statusCode()));
            }
        })).exceptionHandler(ex -> onError.accept(new IllegalStateException(ex))).putHeader(X_VAULT_TOKEN, properties.getToken()).end(requestStr);
    }

}
