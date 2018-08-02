package com.github.fabmrc.eaas.api.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EncryptVaultResponse {

    private List<String> cipheredValues;

    @SuppressWarnings("unchecked")
    @JsonProperty("data")
    private void unpackNested(Map<String, Object> datas) {
        List<Map<String, String>> results = (List<Map<String, String>>) datas.get("batch_results");
        cipheredValues = results.stream().map(x -> x.values()).flatMap(x -> x.stream()).collect(Collectors.toList());
    }

    public List<String> getCipheredValues() {
        return cipheredValues;
    }
}

