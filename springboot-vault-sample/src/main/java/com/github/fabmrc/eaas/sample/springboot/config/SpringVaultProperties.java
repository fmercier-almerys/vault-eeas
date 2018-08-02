package com.github.fabmrc.eaas.sample.springboot.config;

import com.github.fabmrc.eaas.api.VaultProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "vault")
public class SpringVaultProperties implements VaultProperties {

    private String host;

    private int port;

    private String key;

    private String encryptEndpoint;

    private String decryptEndpoint;

    private String token;

    private List<String> fields = new ArrayList<>();

    public List<String> getFields() {
        return fields;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDecryptionUrl() {
        return String.join("/", host, decryptEndpoint, key);
    }

    @Override
    public String getEncryptionUrl() {
        return String.join("/", host, encryptEndpoint, key);
    }

    @Override
    public int getPort() {
        return port;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEncryptEndpoint() {
        return encryptEndpoint;
    }

    public void setEncryptEndpoint(String encryptEndpoint) {
        this.encryptEndpoint = encryptEndpoint;
    }

    public String getDecryptEndpoint() {
        return decryptEndpoint;
    }

    public void setDecryptEndpoint(String decryptEndpoint) {
        this.decryptEndpoint = decryptEndpoint;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
