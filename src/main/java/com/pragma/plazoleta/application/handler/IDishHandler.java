package com.pragma.plazoleta.application.handler;

import com.pragma.plazoleta.application.dto.request.DishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishStatusRequestDto;

public interface IDishHandler {
    void saveDish(DishRequestDto dishRequestDto, Long userId, String rol);
    void updateDish(UpdateDishRequestDto updateDishRequestDto, Long userId, String rol);
    void updateDishStatus(UpdateDishStatusRequestDto updateDishStatusRequestDto, Long userId, String rol, Long dishId);
}
