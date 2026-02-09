package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.pragma.plazoleta.application.dto.response.OrderResponseDto;
import com.pragma.plazoleta.application.handler.IOrderHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/plazoleta/order")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/")
    public ResponseEntity<OrderResponseDto> saveOrder(
            @RequestAttribute("auth.userId") Long userId,
            @Valid @RequestBody CreateOrderRequestDto orderRequestDto) {
        orderHandler.saveOrder(orderRequestDto, userId);
        return new ResponseEntity<OrderResponseDto>(HttpStatus.CREATED);
    }
}
