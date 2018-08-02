package com.github.fabmrc.eaas.api;

import com.github.fabmrc.eaas.api.jackson.DecryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.DecryptVaultResponse;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultResponse;

import java.util.List;

public class VaultEncryptEngine implements EncryptEngine {

    private VaultDriver vaultDriver;

    public VaultEncryptEngine(VaultDriver vaultDriver) {
        this.vaultDriver = vaultDriver;
    }

    @Override
    public EnhancedJsonNode encrypt(EnhancedJsonNode node, List<String> pointers) {
        List<String> values = node.getValues(pointers);
        EncryptVaultRequest request = EncryptVaultRequest.build(values);
        EncryptVaultResponse response = vaultDriver.encrypt(request);
        node.replaceValues(pointers, response.getCipheredValues());
        return node;
    }

    @Override
    public EnhancedJsonNode decrypt(EnhancedJsonNode node, List<String> pointers) {
        List<String> values = node.getValues(pointers);
        DecryptVaultRequest request = DecryptVaultRequest.build(values);
        DecryptVaultResponse response = vaultDriver.decrypt(request);
        node.replaceValues(pointers, response.getValues());
        return node;
    }
}
