package com.pragma.plazoleta.application.handler;

import com.pragma.plazoleta.application.dto.request.DishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishRequestDto;
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
public class DishHandlerTest {
    @Mock
    private DishUseCase dishUseCase;

    @Mock
    private IDishRequestMapper dishRequestMapper;

    @InjectMocks
    private DishHandler dishHandler;


    @Test
    void shouldSaveDishSuccessfully() {
        // arrange
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

        // act
        dishHandler.saveDish(requestDto, userId, rol);

        // assert
        verify(dishRequestMapper).toDish(requestDto);
        verify(dishUseCase).saveDish(dish, userId, rol);
        verifyNoMoreInteractions(dishUseCase, dishRequestMapper);
    }

    @Test
    void shouldUpdateDishSuccessfully() {
        // arrange
        Long userId = 5L;
        String rol = "PROPIETARIO";

        UpdateDishRequestDto dto = new UpdateDishRequestDto();
        dto.setRestaurantId(10L);
        dto.setDishId(1L);
        dto.setPrice(30000);
        dto.setDescription("Updated description");

        // act
        dishHandler.updateDish(dto, userId, rol);

        // assert
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
}
