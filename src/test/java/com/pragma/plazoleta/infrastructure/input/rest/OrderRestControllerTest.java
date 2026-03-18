package com.pragma.plazoleta.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.pragma.plazoleta.application.dto.request.OrderItemRequestDto;
import com.pragma.plazoleta.application.dto.response.ListOrderResponseDto;
import com.pragma.plazoleta.application.dto.response.OrderResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
import com.pragma.plazoleta.application.handler.IOrderHandler;
import com.pragma.plazoleta.domain.model.OrderStatus;
import com.pragma.plazoleta.domain.spi.IJwtPersistencePort;
import com.pragma.plazoleta.infrastructure.input.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@WebMvcTest(
        controllers = OrderRestController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IOrderHandler orderHandler;

    @MockBean
    private IJwtPersistencePort jwtPersistencePort;

    private static final String BASE_URL = "/api/v1/plazoleta/order";

    @Test
    @WithMockUser(authorities = "CLIENT")
    void shouldReturn201WhenOrderIsCreated() throws Exception {
        Long userId = 10L;
        String email = "cliente@test.com";

        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setDishId(1L);
        item.setQuantity(2);

        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();
        requestDto.setRestaurantId(1L);
        requestDto.setItems(List.of(item));

        OrderResponseDto responseDto = new OrderResponseDto();

        when(orderHandler.saveOrder(any(CreateOrderRequestDto.class), eq(userId),eq(email)))
                .thenReturn(responseDto);

        mockMvc.perform(
                        post(BASE_URL + "/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .requestAttr("auth.userId", userId)
                                .requestAttr("auth.email", email)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isCreated());

        verify(orderHandler)
                .saveOrder(any(CreateOrderRequestDto.class), eq(userId),eq(email));
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    void shouldReturnOrdersByStatus() throws Exception {

        Long userId = 5L;
        String status = "PENDIENTE";
        int page = 0;
        int size = 10;

        ListOrderResponseDto order = new ListOrderResponseDto();
        order.setId(1L);
        order.setClientId(20L);
        order.setRestaurantId(1L);
        order.setStatus(status);

        PageResponseDto<ListOrderResponseDto> response =
                new PageResponseDto<>(
                        List.of(order),
                        page,
                        size,
                        1L,
                        1,
                        true
                );

        when(orderHandler.listOrderByStatus(userId, status, page, size))
                .thenReturn(response);

        mockMvc.perform(
                        get(BASE_URL)
                                .param("status", status)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                                .requestAttr("auth.userId", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(orderHandler)
                .listOrderByStatus(userId, status, page, size);
        verifyNoMoreInteractions(orderHandler);
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    void shouldUpdateOrderStatus() throws Exception {

        Long userId = 5L;
        Long orderId = 1L;

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(orderId);
        responseDto.setStatus(OrderStatus.EN_PREPARACION);

        when(orderHandler.updateStatusOrder(userId, orderId))
                .thenReturn(responseDto);

        mockMvc.perform(
                        patch(BASE_URL + "/" + orderId + "/status")
                                .requestAttr("auth.userId", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(orderHandler)
                .updateStatusOrder(userId, orderId);

        verifyNoMoreInteractions(orderHandler);
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    void shouldUpdateOrderStatusToReady() throws Exception {

        Long userId = 5L;
        Long orderId = 1L;

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(orderId);
        responseDto.setStatus(OrderStatus.LISTO); // o el status que uses

        when(orderHandler.updateStatusOrderReady(userId, orderId))
                .thenReturn(responseDto);

        mockMvc.perform(
                        patch(BASE_URL + "/" + orderId + "/statusReady")
                                .requestAttr("auth.userId", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(orderHandler)
                .updateStatusOrderReady(userId, orderId);

        verifyNoMoreInteractions(orderHandler);
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    void shouldUpdateOrderStatusToDelivery() throws Exception {

        Long userId = 5L;
        Long orderId = 1L;
        String code = "202420";

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(orderId);
        responseDto.setStatus(OrderStatus.ENTREGADO); // o el status que uses

        doNothing().when(orderHandler)
                .updateStatusOrderDelivery(code, userId, orderId);

        mockMvc.perform(
                        patch(BASE_URL + "/" + orderId + "/statusDelivery")
                                .param("code", code)
                                .requestAttr("auth.userId", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(orderHandler)
                .updateStatusOrderDelivery(code, userId, orderId);

        verifyNoMoreInteractions(orderHandler);
    }

    @Test
    @WithMockUser(authorities = "CLIENT")
    void shouldUpdateOrderStatusToCanceled() throws Exception {

        Long userId = 5L;
        Long orderId = 1L;


        doNothing().when(orderHandler)
                .updateStatusOrderCanceled(userId, orderId);

        mockMvc.perform(
                        patch(BASE_URL + "/" + orderId + "/statusCanceled")
                                .requestAttr("auth.userId", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(orderHandler)
                .updateStatusOrderCanceled(userId, orderId);

        verifyNoMoreInteractions(orderHandler);
    }
    @Test
    @DisplayName("Debe retornar lista de IDs de órdenes y estatus 200 OK")
    void shouldGetOrderIdsByRestaurantId() throws Exception {
        // GIVEN
        Long restaurantId = 1L;
        List<Long> orderIds = List.of(101L, 102L);

        when(orderHandler.getOrdersByRestaurantId(restaurantId))
                .thenReturn(orderIds);

        // WHEN
        // Si tu path final es "/api/v1/order/restaurant/1/orders"
        mockMvc.perform(
                        get(BASE_URL + "/restaurant/" + restaurantId + "/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                // THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(orderHandler).getOrdersByRestaurantId(restaurantId);
    }

}
