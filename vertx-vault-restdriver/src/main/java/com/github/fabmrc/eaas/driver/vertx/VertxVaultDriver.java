package com.github.fabmrc.eaas.driver.vertx;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.HttpClient;
import com.github.fabmrc.eaas.api.AsyncVaultDriver;
import com.github.fabmrc.eaas.api.VaultProperties;
import com.github.fabmrc.eaas.api.jackson.DecryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.DecryptVaultResponse;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultResponse;
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
        String requestStr = objectMapper.writeValueAsString(request);
        httpClient.post(String.join("/", properties.getEncryptionUrl(), properties.getKey()), clientResponse -> clientResponse.bodyHandler(totalBuffer -> {
            String responseBuffer = totalBuffer.getString(0, totalBuffer.length());
            logger.debug("response :" + clientResponse.statusCode() + "; " + responseBuffer);
            if (clientResponse.statusCode() == 200) {
                EncryptVaultResponse vaultResponse;
                try {
                    vaultResponse = objectMapper.readValue(responseBuffer, EncryptVaultResponse.class);
                    onResponse.accept(vaultResponse);
                } catch (IOException e) {
                    onError.accept(e);
                }
            } else {
                onError.accept(new IllegalStateException());
            }
        })).exceptionHandler(ex -> onError.accept(new IllegalStateException(ex))).putHeader(X_VAULT_TOKEN, properties.getToken()).end(requestStr);
    }

    @Override
    public void decrypt(DecryptVaultRequest request, Consumer<DecryptVaultResponse> onResponse, Consumer<Exception> onError) throws Exception {
        String requestStr = objectMapper.writeValueAsString(request);
        httpClient.post(String.join("/", properties.getDecryptionUrl(), properties.getKey()), clientResponse -> clientResponse.bodyHandler(totalBuffer -> {
            String responseBuffer = totalBuffer.getString(0, totalBuffer.length());
            logger.debug("response :" + clientResponse.statusCode() + "; " + responseBuffer);
            if (clientResponse.statusCode() == 200) {
                DecryptVaultResponse vaultResponse;
                try {
                    vaultResponse = objectMapper.readValue(responseBuffer, DecryptVaultResponse.class);
                    onResponse.accept(vaultResponse);
                } catch (IOException e) {
                    onError.accept(e);
                }
            } else {
                onError.accept(new IllegalStateException());
            }
        })).exceptionHandler(ex -> onError.accept(new IllegalStateException(ex))).putHeader(X_VAULT_TOKEN, properties.getToken()).end(requestStr);
    }

}
