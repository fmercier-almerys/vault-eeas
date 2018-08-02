package com.github.fabmrc.eaas.sample.springboot.config;


import com.github.fabmrc.eaas.sample.springboot.EncryptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabmrc.eaas.driver.spring.SpringVaultDriver;
import com.github.fabmrc.eaas.api.EncryptEngine;
import com.github.fabmrc.eaas.api.VaultDriver;
import com.github.fabmrc.eaas.api.VaultEncryptEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class VaultConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public EncryptService encryptService(ObjectMapper objectMapper, SpringVaultProperties vaultProperties, EncryptEngine engine) {
        return new EncryptService(objectMapper, vaultProperties, engine);
    }

    @Bean
    public EncryptEngine encryptEngine(VaultDriver vaultDriver) {
        return new VaultEncryptEngine(vaultDriver);
    }

    @Bean
    public VaultDriver vaultDriver(SpringVaultProperties vaultProperties) {
        return new SpringVaultDriver(new RestTemplate(), vaultProperties);
    }
}
