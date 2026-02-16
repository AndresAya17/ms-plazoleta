package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageResultTest {

    @Test
    void shouldCalculateTotalPagesCorrectlyWhenExactDivision() {
        PageResult<String> pageResult =
                new PageResult<>(
                        List.of("item1"),
                        0,
                        10,
                        20L
                );

        assertEquals(2, pageResult.getTotalPages());
    }

    @Test
    void shouldThrowExceptionWhenPageIsNegative() {
        DomainException exception = assertThrows(
                DomainException.class,
                () -> new PageResult<>(
                        List.of("item"),
                        -1,
                        10,
                        10L
                )
        );

        assertEquals(ErrorCode.INVALID_PAGINATION, exception.getErrorCode());
        assertEquals("Invalid pagination parameters", exception.getMessage());
    }
}
