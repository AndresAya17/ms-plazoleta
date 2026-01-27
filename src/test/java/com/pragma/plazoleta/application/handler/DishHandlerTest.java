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
        DishRequestDto requestDto = new DishRequestDto();
        requestDto.setName("Pizza");
        requestDto.setPrice(25000);
        requestDto.setDescription("Delicious pizza");
        requestDto.setImageUrl("http://image.com/pizza.jpg");
        requestDto.setCategory(DishCategory.MAIN_COURSE);
        requestDto.setRestaurantId(10L);
        requestDto.setOwnerId(5L);

        Dish dish = new Dish(
                null,
                "Pizza",
                25000,
                "Delicious pizza",
                "http://image.com/pizza.jpg",
                DishCategory.MAIN_COURSE,
                10L,
                5L
        );

        when(dishRequestMapper.toDish(requestDto)).thenReturn(dish);

        // act
        dishHandler.saveDish(requestDto);

        // assert
        verify(dishRequestMapper).toDish(requestDto);
        verify(dishUseCase, times(1)).saveDish(dish);
        verifyNoMoreInteractions(dishUseCase, dishRequestMapper);
    }

    @Test
    void shouldUpdateDishSuccessfully() {
        // arrange
        UpdateDishRequestDto dto = new UpdateDishRequestDto();
        dto.setDishId(1L);
        dto.setPrice(30000);
        dto.setDescription("Updated description");

        // act
        dishHandler.updateDish(dto);

        // assert
        verify(dishUseCase, times(1))
                .updateDish(1L, 30000, "Updated description");

        verifyNoMoreInteractions(dishUseCase);
    }
}
