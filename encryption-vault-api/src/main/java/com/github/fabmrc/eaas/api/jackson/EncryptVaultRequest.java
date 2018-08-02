package com.github.fabmrc.eaas.api.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class EncryptVaultRequest {

    @JsonProperty("batch_input")
    private List<EncryptBatchInput> batchInput;

    public EncryptVaultRequest(List<EncryptBatchInput> batchInput) {
        this.batchInput = batchInput;
    }

    public List<EncryptBatchInput> getBatchInput() {
        return batchInput;
    }

    public static class EncryptBatchInput {

        private String plaintext;

        public EncryptBatchInput(String plaintext) {
            this.plaintext = Base64.getEncoder().encodeToString(plaintext.getBytes());
        }

        public String getPlaintext() {
            return plaintext;
        }
    }

    public static EncryptVaultRequest build(List<String> values) {
        List<EncryptBatchInput> batchInputs = values.stream().map(EncryptBatchInput::new).collect(Collectors.toList());
        return new EncryptVaultRequest(batchInputs);
    }

}

