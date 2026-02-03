package com.pragma.plazoleta.application.dto.response;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
class EmployeeResponseDtoTest {

    @Test
    void shouldReturnEmployeeUserIdWhenGetterIsCalled() {
        EmployeeResponseDto dto = new EmployeeResponseDto();
        Long expectedUserId = 10L;

        ReflectionTestUtils.setField(dto, "employeeUserId", expectedUserId);

        Long result = dto.getEmployeeUserId();

        assertEquals(expectedUserId, result);
    }
}
