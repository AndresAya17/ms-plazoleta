package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.pragma.plazoleta.application.dto.response.ListOrderResponseDto;
import com.pragma.plazoleta.application.dto.response.OrderResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
import com.pragma.plazoleta.application.handler.IOrderHandler;
import com.pragma.plazoleta.infrastructure.util.constants.SecurityConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/plazoleta/order")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @Operation(
            summary = "Create a new order",
            description = """
                Creates a new order for the authenticated client.
                
                - The client must have the CLIENT role.
                - The userId is extracted from the authentication token.
                - The order must contain at least one dish.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Missing or invalid authentication token"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User does not have CLIENT role"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant or dish not found"
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize(SecurityConstants.HAS_CLIENT)
    @PostMapping("/")
    public ResponseEntity<OrderResponseDto> saveOrder(
            @RequestAttribute("auth.userId") Long userId,
            @Valid @RequestBody CreateOrderRequestDto orderRequestDto) {
        orderHandler.saveOrder(orderRequestDto, userId);
        return new ResponseEntity<OrderResponseDto>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "List orders by status",
            description = """
                Returns a paginated list of orders filtered by status 
                for the authenticated employee.

                - Requires EMPLOYEE role.
                - User ID is extracted from JWT token.
                - Results are paginated.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orders retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PageResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid status or pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires EMPLOYEE role")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize(SecurityConstants.HAS_EMPLOYEE)
    @GetMapping()
    public ResponseEntity<PageResponseDto<ListOrderResponseDto>> listOrderByStatus(
            @RequestAttribute("auth.userId") Long userId,
            @RequestParam String status,
            @RequestParam int page,
            @RequestParam int size) {
        return ResponseEntity.ok(
                orderHandler.listOrderByStatus(userId, status, page, size)
        );
    }

    @Operation(
            summary = "Update order status",
            description = """
                Updates the status of an order according to the business flow.

                - Requires EMPLOYEE role.
                - User ID is extracted from JWT.
                - Order must exist.
                - Status transition must be valid.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires EMPLOYEE role"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize(SecurityConstants.HAS_EMPLOYEE)
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateStatusOrder(
            @PathVariable Long orderId,
            @RequestAttribute("auth.userId") Long userId) {

        return ResponseEntity.ok(
                orderHandler.updateStatusOrder(userId, orderId)
        );
    }

    @Operation(
            summary = "Mark order as READY",
            description = """
                Marks an order as READY.

                - Requires EMPLOYEE role.
                - User ID is extracted from JWT.
                - Order must be in IN_PREPARATION state.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order marked as READY successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid state transition"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires EMPLOYEE role"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize(SecurityConstants.HAS_EMPLOYEE)
    @PatchMapping("/{orderId}/statusReady")
    public ResponseEntity<OrderResponseDto> updateStatusOrderReady(
            @PathVariable Long orderId,
            @RequestAttribute("auth.userId") Long userId) {

        return ResponseEntity.ok(
                orderHandler.updateStatusOrderReady(userId, orderId)
        );
    }
}
