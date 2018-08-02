package com.github.fabmrc.eaas.api;

import com.github.fabmrc.eaas.api.jackson.DecryptVaultResponse;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultResponse;

import java.util.List;
import java.util.function.Consumer;

public interface AsyncEncryptEngine {

    void encrypt(EnhancedJsonNode jsonNode, List<String> pointers, Consumer<EncryptVaultResponse> onSuccess, Consumer<Exception> onError) throws Exception;

    void decrypt(EnhancedJsonNode jsonNode, List<String> pointers, Consumer<DecryptVaultResponse> onSuccess, Consumer<Exception> onError) throws Exception;
}
