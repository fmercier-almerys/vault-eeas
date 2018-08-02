package com.github.fabmrc.eaas.api.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class DecryptVaultRequest {

    @JsonProperty("batch_input")
    private List<DecryptBatchInput> batchInput;

    public DecryptVaultRequest(List<DecryptBatchInput> batchInput) {
        this.batchInput = batchInput;
    }

    public List<DecryptBatchInput> getBatchInput() {
        return batchInput;
    }

    public static class DecryptBatchInput {

        private String ciphertext;

        public DecryptBatchInput(String ciphertext) {
            this.ciphertext = ciphertext;
        }

        public String getCiphertext() {
            return ciphertext;
        }
    }

    public static DecryptVaultRequest build(List<String> values) {
        List<DecryptVaultRequest.DecryptBatchInput> batchInputs = values.stream().map(DecryptVaultRequest.DecryptBatchInput::new).collect(Collectors.toList());
        return new DecryptVaultRequest(batchInputs);
    }
}
