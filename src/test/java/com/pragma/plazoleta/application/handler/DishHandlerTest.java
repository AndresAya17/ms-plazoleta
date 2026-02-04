package com.pragma.plazoleta.application.handler;

import com.pragma.plazoleta.application.dto.request.DishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishStatusRequestDto;
import com.pragma.plazoleta.application.handler.impl.DishHandler;
import com.pragma.plazoleta.application.mapper.IDishRequestMapper;
import com.pragma.plazoleta.domain.api.IDishServicePort;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.DishCategory;
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
        String rol = "PROPIETARIO";

        DishRequestDto requestDto = new DishRequestDto();
        requestDto.setName("Pizza");
        requestDto.setPrice(25000);
        requestDto.setDescription("Delicious pizza");
        requestDto.setImageUrl("http://image.com/pizza.jpg");
        requestDto.setCategory(DishCategory.MAIN_COURSE);
        requestDto.setRestaurantId(10L);

        Dish.DishInfo dishInfo = new Dish.DishInfo(
                null,
                "Pizza",
                25000,
                "Delicious pizza",
                "http://image.com/pizza.jpg",
                DishCategory.MAIN_COURSE
        );

        Dish dish = new Dish(
                dishInfo,
                requestDto.getRestaurantId(),
                userId
        );

        when(dishRequestMapper.toDish(requestDto)).thenReturn(dish);

        dishHandler.saveDish(requestDto, userId, rol);

        verify(dishRequestMapper).toDish(requestDto);
        verify(dishUseCase).saveDish(dish, userId, rol);
        verifyNoMoreInteractions(dishUseCase, dishRequestMapper);
    }

    @Test
    void shouldUpdateDishSuccessfully() {
        Long userId = 5L;
        String rol = "PROPIETARIO";

        UpdateDishRequestDto dto = new UpdateDishRequestDto();
        dto.setRestaurantId(10L);
        dto.setDishId(1L);
        dto.setPrice(30000);
        dto.setDescription("Updated description");

        dishHandler.updateDish(dto, userId, rol);

        verify(dishUseCase).updateDish(
                10L,
                1L,
                30000,
                "Updated description",
                userId,
                rol
        );

        verifyNoMoreInteractions(dishUseCase);
    }

    @Test
    void shouldUpdateDishStatus(){
        Long userId = 5L;
        String rol = "PROPIETARIO";
        Long dishId = 1L;
        UpdateDishStatusRequestDto dto = new UpdateDishStatusRequestDto();
        dto.setActive(true);

        dishHandler.updateDishStatus(dto,userId,rol,dishId);

        verify(dishUseCase).updateDishStatus(true,5L,"PROPIETARIO",1L);

        verifyNoMoreInteractions(dishUseCase);
    }
}
