package com.github.fabmrc.eaas.sample.springboot;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class VaultController {

    private EncryptService vaultSerializer;

    public VaultController(EncryptService vaultSerializer) {
        this.vaultSerializer = vaultSerializer;
    }

    @PostMapping(value = "/encrypt")
    public String encrypt(@RequestBody String originRequest) throws IOException {
        return vaultSerializer.encryptRequest(originRequest);
    }

    @PostMapping(value = "/decrypt")
    public String decrypt(@RequestBody String originRequest) throws IOException {
        return vaultSerializer.decryptRequest(originRequest);
    }

}
