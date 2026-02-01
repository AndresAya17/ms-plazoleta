package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IRestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantJpaAdapterTest {
    @Mock
    private IRestaurantRepository restaurantRepository;

    @Mock
    private IRestaurantEntityMapper restaurantEntityMapper;

    @InjectMocks
    private RestaurantJpaAdapter restaurantJpaAdapter;

    @Test
    void shouldSaveRestaurantAndReturnMappedRestaurant() {
        // arrange
        Restaurant domainRestaurant = new Restaurant();
        domainRestaurant.setName("Restaurante Test");

        RestaurantEntity entityToSave = new RestaurantEntity();
        entityToSave.setName("Restaurante Test");

        RestaurantEntity savedEntity = new RestaurantEntity();
        savedEntity.setId(1L);
        savedEntity.setName("Restaurante Test");

        Restaurant mappedBackRestaurant = new Restaurant();
        mappedBackRestaurant.setId(1L);
        mappedBackRestaurant.setName("Restaurante Test");

        when(restaurantEntityMapper.toEntity(domainRestaurant))
                .thenReturn(entityToSave);

        when(restaurantRepository.save(entityToSave))
                .thenReturn(savedEntity);

        when(restaurantEntityMapper.toRestaurant(savedEntity))
                .thenReturn(mappedBackRestaurant);

        // act
        Restaurant result = restaurantJpaAdapter.saveRestaurant(domainRestaurant);

        // assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Restaurante Test", result.getName());

        verify(restaurantEntityMapper, times(1)).toEntity(domainRestaurant);
        verify(restaurantRepository, times(1)).save(entityToSave);
        verify(restaurantEntityMapper, times(1)).toRestaurant(savedEntity);
        verifyNoMoreInteractions(
                restaurantRepository,
                restaurantEntityMapper
        );
    }
    @Test
    void shouldFindRestaurantByIdAndReturnMappedRestaurant() {
        Long restaurantId = 1L;

        RestaurantEntity entityFound = new RestaurantEntity();
        entityFound.setId(restaurantId);
        entityFound.setName("Restaurante Test");

        Restaurant mappedRestaurant = new Restaurant();
        mappedRestaurant.setId(restaurantId);
        mappedRestaurant.setName("Restaurante Test");

        when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.of(entityFound));

        when(restaurantEntityMapper.toRestaurant(entityFound))
                .thenReturn(mappedRestaurant);

        // act
        Optional<Restaurant> result = restaurantJpaAdapter.findById(restaurantId);

        // assert
        assertTrue(result.isPresent());

        Restaurant restaurant = result.get();
        assertEquals(restaurantId, restaurant.getId());
        assertEquals("Restaurante Test", restaurant.getName());

        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(restaurantEntityMapper, times(1)).toRestaurant(entityFound);
        verifyNoMoreInteractions(restaurantRepository, restaurantEntityMapper);
    }

}
