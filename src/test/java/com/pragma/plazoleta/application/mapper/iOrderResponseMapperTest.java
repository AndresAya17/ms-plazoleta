package com.pragma.plazoleta.application.mapper;
import com.pragma.plazoleta.application.dto.response.OrderResponseDto;
import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.model.OrderStatus;
import com.pragma.plazoleta.domain.model.PageResult;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class iOrderResponseMapperTest {

    private final IOrderResponseMapper mapper =
            Mappers.getMapper(IOrderResponseMapper.class);

    @Test
    void shouldMapOrderToOrderResponseDto() {

        Order order = new Order(
                1L,
                1L,
                List.of()
        );
        order.setId(1L);
        order.setStatus(OrderStatus.PENDIENTE);

        OrderResponseDto response = mapper.toResponse(order);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(OrderStatus.PENDIENTE, response.getStatus());
    }

    @Test
    void shouldMapPageResultToPageResponseDto() {

        Order order = new Order(
                1L,
                1L,
                List.of()
        );

        order.setId(1L);
        order.setStatus(OrderStatus.PENDIENTE);

        PageResult<Order> pageResult =
                new PageResult<>(
                        List.of(order),
                        0,
                        1,
                        1L
                );

        var response = mapper.listToResponse(pageResult);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(1L, response.getContent().get(0).getId());
        assertEquals(OrderStatus.PENDIENTE.name(), response.getContent().get(0).getStatus());
    }

}
