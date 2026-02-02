package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.application.dto.request.DishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishStatusRequestDto;
import com.pragma.plazoleta.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/plazoleta/dish")
@RequiredArgsConstructor
public class DishRestController {
    private final IDishHandler dishHandler;

    @Operation(summary = "add dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "dish created"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
    })
    @PostMapping("/")
    public ResponseEntity<Void> saveDish(
            @RequestAttribute("auth.userId") Long userId,
            @RequestAttribute("auth.rol") String rol,
            @Valid @RequestBody DishRequestDto dishRequestDto){
        dishHandler.saveDish(dishRequestDto, userId, rol);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/")
    public ResponseEntity<Void> updateDish(
            @RequestAttribute("auth.userId") Long userId,
            @RequestAttribute("auth.rol") String rol,
            @Valid @RequestBody UpdateDishRequestDto updateDishRequestDto){
        dishHandler.updateDish(updateDishRequestDto, userId, rol);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateDishStatus(
            @PathVariable("id") Long dishId,
            @RequestAttribute("auth.userId") Long userId,
            @RequestAttribute("auth.rol") String rol,
            @Valid @RequestBody UpdateDishStatusRequestDto updateDishRequestDto){
        dishHandler.updateDishStatus(updateDishRequestDto, userId, rol, dishId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
