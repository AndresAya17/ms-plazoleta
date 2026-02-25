package com.pragma.plazoleta.infrastructure.util;

import com.pragma.plazoleta.domain.spi.ICodeGeneratorPort;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public final class CodeGenerator implements ICodeGeneratorPort {

    private static final SecureRandom secureRandom = new SecureRandom();

    private CodeGenerator() {
    }

    @Override
    public String generateSixDigits() {
        int number = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(number);
    }
}
