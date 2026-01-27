package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.application.dto.request.DishRequestDto;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.handler.IDishHandler;
import com.pragma.plazoleta.application.handler.IRestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/plazoleta")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;
    private final IDishHandler dishHandler;

    @Operation(summary = "add restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant created"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "User is not owner"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/restaurant")
    public ResponseEntity<Void> saveUser(@Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        restaurantHandler.saveRestaurant(restaurantRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/dish")
    public ResponseEntity<Void> saveDish(@Valid @RequestBody DishRequestDto dishRequestDto){
        dishHandler.saveDish(dishRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
