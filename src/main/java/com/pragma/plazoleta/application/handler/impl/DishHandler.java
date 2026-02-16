package com.pragma.plazoleta.application.handler.impl;

import com.pragma.plazoleta.application.dto.request.DishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishStatusRequestDto;
import com.pragma.plazoleta.application.handler.IDishHandler;
import com.pragma.plazoleta.application.mapper.IDishRequestMapper;
import com.pragma.plazoleta.domain.api.IDishServicePort;
import com.pragma.plazoleta.domain.model.Dish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;

    @Override
    public void saveDish(DishRequestDto dishRequestDto, Long userId) {
        Dish dish = dishRequestMapper.toDish(dishRequestDto);
        dishServicePort.saveDish(dish, userId);
    }

    @Override
    public void updateDish(UpdateDishRequestDto dto, Long userId) {
        dishServicePort.updateDish(dto.getRestaurantId() ,dto.getDishId(), dto.getPrice(), dto.getDescription(), userId);
    }

    @Override
    public void updateDishStatus(UpdateDishStatusRequestDto updateDishStatusRequestDto, Long userId, Long dishId) {
        dishServicePort.updateDishStatus(updateDishStatusRequestDto.getActive(), userId, dishId);
    }
}
