package com.github.fabmrc.eaas.api;

import com.github.fabmrc.eaas.api.jackson.DecryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.DecryptVaultResponse;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultRequest;
import com.github.fabmrc.eaas.api.jackson.EncryptVaultResponse;

public interface VaultDriver {

    EncryptVaultResponse encrypt(EncryptVaultRequest request);

    DecryptVaultResponse decrypt(DecryptVaultRequest request);
}
