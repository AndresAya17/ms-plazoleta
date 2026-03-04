package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IRestaurantPageMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IRestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
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

    @Mock
    private IRestaurantPageMapper restaurantPageMapper;

    @Test
    void shouldSaveRestaurantAndReturnMappedRestaurant() {
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

        Restaurant result = restaurantJpaAdapter.saveRestaurant(domainRestaurant);

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

        Optional<Restaurant> result = restaurantJpaAdapter.findById(restaurantId);

        assertTrue(result.isPresent());

        Restaurant restaurant = result.get();
        assertEquals(restaurantId, restaurant.getId());
        assertEquals("Restaurante Test", restaurant.getName());

        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(restaurantEntityMapper, times(1)).toRestaurant(entityFound);
        verifyNoMoreInteractions(restaurantRepository, restaurantEntityMapper);
    }

    @Test
    void shouldListRestaurantsOrderedByNameAscending() {
        int page = 0;
        int size = 10;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante A");

        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(1L);
        entity.setName("Restaurante A");

        Page<RestaurantEntity> entityPage =
                new PageImpl<>(List.of(entity));

        when(restaurantRepository.findAllByOrderByNameAsc(any(Pageable.class)))
                .thenReturn(entityPage);

        when(restaurantPageMapper.toDomain(any(), any()))
                .thenReturn(new PageResult<>(List.of(restaurant), page, size, 1L));

        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(Pageable.class);

        PageResult<Restaurant> result =
                restaurantJpaAdapter.listRestaurants(page, size);

        verify(restaurantRepository)
                .findAllByOrderByNameAsc(pageableCaptor.capture());

        Pageable pageableUsed = pageableCaptor.getValue();

        assertEquals(page, pageableUsed.getPageNumber());
        assertEquals(size, pageableUsed.getPageSize());

        assertEquals(1, result.getContent().size());
    }

}
