package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.application.dto.request.CreateEmployeeRestaurantRequestDto;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.dto.response.DishResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
import com.pragma.plazoleta.application.dto.response.RestaurantListResponseDto;
import com.pragma.plazoleta.application.handler.IRestaurantHandler;
import com.pragma.plazoleta.infrastructure.util.constants.SecurityConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize(SecurityConstants.HAS_ADMIN)
    @PostMapping("/")
    public ResponseEntity<Void> saveRestaurant(
            @Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        restaurantHandler.saveRestaurant(restaurantRequestDto);
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
    @PreAuthorize(SecurityConstants.HAS_CLIENT)
    @GetMapping("/restaurants")
    public ResponseEntity<PageResponseDto<RestaurantListResponseDto>> listRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                restaurantHandler.listRestaurants(page, size)
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
            @ApiResponse(responseCode = "403", description = "Forbidden - user does not have CLIENT role"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @PreAuthorize(SecurityConstants.HAS_CLIENT)
    @GetMapping("/{id}/dishes")
    public ResponseEntity<PageResponseDto<DishResponseDto>> listDish(
            @PathVariable("id") Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId
    ) {
        return ResponseEntity.ok(
                restaurantHandler.listDish(page, size, restaurantId, categoryId)
        );
    }

    @Operation(
            summary = "Validate restaurant owner",
            description = """
                Validates whether a user is the owner of a specific restaurant.

                - Used for authorization validation.
                - Returns 200 if the user is the owner.
                - Throws an error if the user is not authorized.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is the owner of the restaurant"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "403", description = "User is not the owner")
    })
    @GetMapping("/{restaurantId}/validate-owner")
    public ResponseEntity<Void> validateOwner(
            @PathVariable Long restaurantId,
            @RequestParam Long userId
    ) {
        restaurantHandler.validateOwner(restaurantId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Assign employee to restaurant",
            description = """
                Assigns an employee to a restaurant.

                - The employee must exist.
                - The restaurant must exist.
                - The employee must have EMPLOYEE role.
                - An employee can belong to only one restaurant.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Restaurant or employee not found"),
            @ApiResponse(responseCode = "409", description = "Employee already assigned to a restaurant"),
            @ApiResponse(responseCode = "403", description = "Unauthorized operation")
    })
    @PostMapping("/employeeRestaurant/")
    public ResponseEntity<Void> createEmployeeRestaurant(
            @Valid @RequestBody CreateEmployeeRestaurantRequestDto employeeRestaurantRequestDto
    ) {
        restaurantHandler.assignEmployeeToRestaurant(employeeRestaurantRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Listar empleados asignados a un restaurante",
            description = "Devuelve una lista con los IDs de todos los empleados que están vinculados actualmente al restaurante proporcionado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de IDs de empleados obtenida con éxito"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - El usuario no tiene permisos suficientes"),
            @ApiResponse(responseCode = "404", description = "Restaurante no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{restaurantId}/employees")
    public ResponseEntity<List<Long>> getEmployeesToAssignRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(restaurantHandler.getEmployeeRestaurant(restaurantId, userId));
    }


}
