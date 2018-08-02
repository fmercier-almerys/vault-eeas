package com.github.fabmrc.eaas.api;

import java.util.List;

public interface EncryptEngine {

    EnhancedJsonNode encrypt(EnhancedJsonNode node, List<String> pointers);

    EnhancedJsonNode decrypt(EnhancedJsonNode node, List<String> pointers);
}
