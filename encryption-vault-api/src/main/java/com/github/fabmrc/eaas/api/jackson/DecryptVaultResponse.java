package com.github.fabmrc.eaas.api.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DecryptVaultResponse {

    private List<String> values;

    @SuppressWarnings("unchecked")
    @JsonProperty("data")
    private void unpackNested(Map<String, Object> datas) {
        List<Map<String, String>> results = (List<Map<String, String>>) datas.get("batch_results");
        values = results.stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .map(this::decodeValue)
                .collect(Collectors.toList());
    }

    private String decodeValue(String encodedValue) {
        return new String(Base64.getDecoder().decode(encodedValue));
    }

    public List<String> getValues() {
        return values;
    }
}

