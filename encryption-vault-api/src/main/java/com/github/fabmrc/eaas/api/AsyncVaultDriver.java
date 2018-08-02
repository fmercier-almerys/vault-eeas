package com.github.fabmrc.eaas.api;

import com.github.fabmrc.eaas.api.jackson.DecryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.DecryptVaultResponse;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultResponse;

import java.util.function.Consumer;

public interface AsyncVaultDriver {

    void encrypt(EncryptVaultRequest request, Consumer<EncryptVaultResponse> onResponse, Consumer<Exception> onError) throws Exception;

    void decrypt(DecryptVaultRequest request, Consumer<DecryptVaultResponse> onResponse, Consumer<Exception> onError) throws Exception;
}
