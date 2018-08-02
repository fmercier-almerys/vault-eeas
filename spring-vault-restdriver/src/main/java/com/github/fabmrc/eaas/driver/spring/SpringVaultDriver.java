package com.github.fabmrc.eaas.driver.spring;

import com.github.fabmrc.eaas.api.VaultDriver;
import com.github.fabmrc.eaas.api.VaultProperties;
import com.github.fabmrc.eaas.api.jackson.DecryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.DecryptVaultResponse;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultResponse;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class SpringVaultDriver implements VaultDriver {

    private static final String X_VAULT_TOKEN = "X-Vault-Token";

    private RestTemplate restTemplate;

    private VaultProperties vaultProperties;

    public SpringVaultDriver(RestTemplate restTemplate, VaultProperties vaultProperties) {
        this.restTemplate = restTemplate;
        this.vaultProperties = vaultProperties;
    }

    private MultiValueMap<String, String> buildHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap();
        headers.put(X_VAULT_TOKEN, Collections.singletonList(vaultProperties.getToken()));
        return headers;
    }

    @Override
    public EncryptVaultResponse encrypt(EncryptVaultRequest request) {
        return restTemplate.postForObject(vaultProperties.getEncryptionUrl(), new HttpEntity(request, buildHeaders()), EncryptVaultResponse.class);
    }

    @Override
    public DecryptVaultResponse decrypt(DecryptVaultRequest request) {
        return restTemplate.postForObject(vaultProperties.getDecryptionUrl(), new HttpEntity(request, buildHeaders()), DecryptVaultResponse.class);
    }
}
