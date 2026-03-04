package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.application.dto.request.DishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishStatusRequestDto;
import com.pragma.plazoleta.application.handler.IDishHandler;
import com.pragma.plazoleta.infrastructure.util.constants.SecurityConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/plazoleta/dish")
@RequiredArgsConstructor
public class DishRestController {
    private final IDishHandler dishHandler;

    @Operation(
            summary = "Create dish",
            description = "Allows a restaurant owner to create a new dish associated with their restaurant. " +
                    "Only users with role PROPIETARIO are allowed."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid dish data"),
            @ApiResponse(responseCode = "403", description = "User is not allowed to create dishes"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize(SecurityConstants.HAS_OWNER)
    @PostMapping("/")
    public ResponseEntity<Void> saveDish(
            @RequestAttribute("auth.userId") Long userId,
            @Valid @RequestBody DishRequestDto dishRequestDto){
        dishHandler.saveDish(dishRequestDto, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update dish",
            description = "Allows a restaurant owner to update dish information such as price or description. " +
                    "Dish status is not modified by this endpoint."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid update data"),
            @ApiResponse(responseCode = "403", description = "User is not allowed to update this dish"),
            @ApiResponse(responseCode = "404", description = "Dish not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize(SecurityConstants.HAS_OWNER)
    @PatchMapping("/")
    public ResponseEntity<Void> updateDish(
            @RequestAttribute("auth.userId") Long userId,
            @Valid @RequestBody UpdateDishRequestDto updateDishRequestDto){
        dishHandler.updateDish(updateDishRequestDto, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Update dish status",
            description = "Allows a restaurant owner to activate or deactivate a dish. " +
                    "Only the dish status is updated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "403", description = "User is not allowed to update dish status"),
            @ApiResponse(responseCode = "404", description = "Dish not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize(SecurityConstants.HAS_OWNER)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateDishStatus(
            @PathVariable("id") Long dishId,
            @RequestAttribute("auth.userId") Long userId,
            @Valid @RequestBody UpdateDishStatusRequestDto updateDishRequestDto){
        dishHandler.updateDishStatus(updateDishRequestDto, userId, dishId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
