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

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
}
