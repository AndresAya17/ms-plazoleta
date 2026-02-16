package com.pragma.plazoleta.application.handler;

import com.pragma.plazoleta.application.dto.request.DishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishStatusRequestDto;
import com.pragma.plazoleta.application.handler.impl.DishHandler;
import com.pragma.plazoleta.application.mapper.IDishRequestMapper;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.usecase.DishUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishHandlerTest {
    @Mock
    private DishUseCase dishUseCase;

    @Mock
    private IDishRequestMapper dishRequestMapper;

    @InjectMocks
    private DishHandler dishHandler;


    @Test
    void shouldSaveDishSuccessfully() {
        Long userId = 5L;

        DishRequestDto requestDto = new DishRequestDto();
        requestDto.setName("Pizza");
        requestDto.setPrice(25000);
        requestDto.setDescription("Delicious pizza");
        requestDto.setImageUrl("http://image.com/pizza.jpg");
        requestDto.setCategory(1L);
        requestDto.setRestaurantId(10L);

        Dish dish = new Dish();


        when(dishRequestMapper.toDish(requestDto)).thenReturn(dish);

        dishHandler.saveDish(requestDto, userId);

        verify(dishRequestMapper).toDish(requestDto);
        verify(dishUseCase).saveDish(dish, userId);
        verifyNoMoreInteractions(dishUseCase, dishRequestMapper);
    }

    @Test
    void shouldUpdateDishSuccessfully() {
        Long userId = 5L;

        UpdateDishRequestDto dto = new UpdateDishRequestDto();
        dto.setRestaurantId(10L);
        dto.setDishId(1L);
        dto.setPrice(30000);
        dto.setDescription("Updated description");

        dishHandler.updateDish(dto, userId);

        verify(dishUseCase).updateDish(
                10L,
                1L,
                30000,
                "Updated description",
                userId
        );

        verifyNoMoreInteractions(dishUseCase);
    }

    @Test
    void shouldUpdateDishStatus(){
        Long userId = 5L;
        Long dishId = 1L;
        UpdateDishStatusRequestDto dto = new UpdateDishStatusRequestDto();
        dto.setActive(true);

        dishHandler.updateDishStatus(dto,userId,dishId);

        verify(dishUseCase).updateDishStatus(true,5L,1L);

        verifyNoMoreInteractions(dishUseCase);
    }
}
