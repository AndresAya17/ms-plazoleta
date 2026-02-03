package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.application.dto.request.RestaurantEmployeeRequestDto;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.dto.response.RestaurantListResponseDto;
import com.pragma.plazoleta.application.handler.IRestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plazoleta/restaurant")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    @Operation(summary = "add restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant created"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "User is not owner"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/")
    public ResponseEntity<Void> saveRestaurant(
            @RequestAttribute("auth.userId") Long userId,
            @RequestAttribute("auth.rol") String rol,
            @Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        restaurantHandler.saveRestaurant(restaurantRequestDto, userId, rol);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{id}/employees")
    public ResponseEntity<Void> saveEmployee(
            @PathVariable("id") Long restaurantId,
            @RequestAttribute("auth.userId") Long userId,
            @RequestAttribute("auth.rol") String rol,
            @Valid @RequestBody RestaurantEmployeeRequestDto restaurantRequestDto) {
        restaurantHandler.saveRestaurantEmployee(restaurantRequestDto, userId, rol, restaurantId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantListResponseDto>> listRestaurants(
            @RequestAttribute("auth.rol") String rol,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                restaurantHandler.listRestaurants(page, size, rol)
        );
    }


}
