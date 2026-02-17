package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.EmployeeRestaurant;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IEmployeeRestaurantEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.EmployeeRestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeRestaurantJpaAdapterTest {
    @Mock
    private EmployeeRestaurantRepository repository;

    @Mock
    private IEmployeeRestaurantEntityMapper mapper;

    @InjectMocks
    private EmployeeRestaurantJpaAdapter employeeRestaurantJpaAdapter;

    @Test
    void shouldSaveEmployeeRestaurant() {
        EmployeeRestaurant employeeRestaurant =
                new EmployeeRestaurant();

        EmployeeRestaurantEntity entity = new EmployeeRestaurantEntity();
        entity.setEmployeeUserId(1L);
        entity.setRestaurantId(10L);

        when(mapper.toEntity(employeeRestaurant))
                .thenReturn(entity);

        employeeRestaurantJpaAdapter.save(employeeRestaurant);

        verify(mapper).toEntity(employeeRestaurant);
        verify(repository).save(entity);
        verifyNoMoreInteractions(mapper, repository);
    }
    @Test
    void shouldReturnRestaurantIdWhenEmployeeExists() {

        Long employeeUserId = 1L;
        Long restaurantId = 10L;

        EmployeeRestaurantEntity entity = new EmployeeRestaurantEntity();
        entity.setEmployeeUserId(employeeUserId);
        entity.setRestaurantId(restaurantId);

        when(repository.findByEmployeeUserId(employeeUserId))
                .thenReturn(Optional.of(entity));

        Optional<Long> result =
                employeeRestaurantJpaAdapter
                        .findRestaurantIdByEmployeeUserId(employeeUserId);

        verify(repository)
                .findByEmployeeUserId(employeeUserId);

        assertTrue(result.isPresent());
        assertEquals(restaurantId, result.get());
    }
}
