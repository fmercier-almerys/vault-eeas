package com.github.fabmrc.eaas.api;

import com.github.fabmrc.eaas.api.jackson.DecryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.DecryptVaultResponse;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultResponse;

import java.util.List;
import java.util.function.Consumer;

public class AsyncVaultEngine implements AsyncEncryptEngine {

    private AsyncVaultDriver vaultDriver;

    public  AsyncVaultEngine(AsyncVaultDriver vaultDriver) {
        this.vaultDriver = vaultDriver;
    }

    @Override
    public void encrypt(EnhancedJsonNode jsonNode, List<String> pointers, Consumer<EncryptVaultResponse> onSuccess, Consumer<Exception> onError) throws Exception {
        List<String> values = jsonNode.getValues(pointers);
        EncryptVaultRequest request = EncryptVaultRequest.build(values);
        vaultDriver.encrypt(request, vaultResponse -> {
            jsonNode.replaceValues(pointers, vaultResponse.getCipheredValues());
            onSuccess.accept(vaultResponse);
        }, onError::accept);
    }

    @Override
    public void decrypt(EnhancedJsonNode jsonNode, List<String> pointers, Consumer<DecryptVaultResponse> onSuccess, Consumer<Exception> onError) throws Exception {
        List<String> values = jsonNode.getValues(pointers);
        DecryptVaultRequest request = DecryptVaultRequest.build(values);
        vaultDriver.decrypt(request, vaultResponse -> {
            jsonNode.replaceValues(pointers, vaultResponse.getValues());
            onSuccess.accept(vaultResponse);
        }, onError::accept);
    }
}
