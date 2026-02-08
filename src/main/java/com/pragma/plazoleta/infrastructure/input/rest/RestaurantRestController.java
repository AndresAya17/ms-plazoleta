package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.application.dto.request.RestaurantEmployeeRequestDto;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.dto.response.DishResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
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

    @Operation(
            summary = "Create restaurant",
            description = "Allows an ADMINISTRADOR to create a new restaurant in the system. " +
                    "The owner must already exist and have the PROPIETARIO role."
    )
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

    @Operation(
            summary = "Create restaurant employee",
            description = "Allows a restaurant owner to create an employee associated with their restaurant."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid employee data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "User does not have PROPIETARIO role"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @PostMapping("/{id}/employees")
    public ResponseEntity<Void> saveEmployee(
            @PathVariable("id") Long restaurantId,
            @RequestAttribute("auth.userId") Long userId,
            @RequestAttribute("auth.rol") String rol,
            @Valid @RequestBody RestaurantEmployeeRequestDto restaurantRequestDto) {
        restaurantHandler.saveRestaurantEmployee(restaurantRequestDto, userId, rol, restaurantId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "List restaurants",
            description = "Returns a paginated and alphabetically ordered list of available restaurants. " +
                    "Only name and logo URL are returned. Accessible by CLIENTE role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurants retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "User does not have CLIENTE role")
    })
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

    @Operation(
            summary = "List dishes by restaurant",
            description = "Returns a paginated list of active dishes for a given restaurant. " +
                    "Dishes can be optionally filtered by category. " +
                    "Accessible only by users with CLIENTE role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dishes retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid authentication"),
            @ApiResponse(responseCode = "403", description = "Forbidden - user does not have CLIENTE role"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/{id}/dishes")
    public ResponseEntity<PageResponseDto<DishResponseDto>> listDish(
            @PathVariable("id") Long restaurantId,
            @RequestAttribute("auth.rol") String rol,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId
    ) {
        return ResponseEntity.ok(
                restaurantHandler.listDish(page, size, rol, restaurantId, categoryId)
        );
    }


}
