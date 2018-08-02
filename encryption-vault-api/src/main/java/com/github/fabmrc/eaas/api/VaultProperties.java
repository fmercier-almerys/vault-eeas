package com.github.fabmrc.eaas.api;

public interface VaultProperties {

    String getKey();

    String getDecryptionUrl();

    String getEncryptionUrl();

    int getPort();

    String getToken();

    String getHost();
}
