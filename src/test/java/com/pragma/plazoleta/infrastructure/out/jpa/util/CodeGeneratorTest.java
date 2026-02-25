package com.pragma.plazoleta.infrastructure.out.jpa.util;

import com.pragma.plazoleta.infrastructure.util.CodeGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CodeGenerator.class)
class CodeGeneratorTest {

    @Autowired
    private CodeGenerator codeGenerator;

    @Test
    void shouldGenerateSixDigitNumber() {

        String code = codeGenerator.generateSixDigits();

        assertNotNull(code);
        assertEquals(6, code.length());
        assertTrue(code.matches("\\d{6}"));

        int numericValue = Integer.parseInt(code);
        assertTrue(numericValue >= 100000);
        assertTrue(numericValue <= 999999);
    }

    @Test
    void shouldGenerateDifferentCodesInMultipleCalls() {

        String code1 = codeGenerator.generateSixDigits();
        String code2 = codeGenerator.generateSixDigits();

        assertNotNull(code1);
        assertNotNull(code2);

        // No garantizamos que siempre sean distintos,
        // pero es extremadamente improbable que coincidan.
        assertNotEquals(code1, code2);
    }
}
