package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.EmployeeRestaurant;
import com.pragma.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IEmployeeRestaurantEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.EmployeeRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmployeeRestaurantJpaAdapter implements IEmployeeRestaurantPersistencePort {

    private final EmployeeRestaurantRepository repository;
    private final IEmployeeRestaurantEntityMapper mapper;

    @Override
    public void save(EmployeeRestaurant employeeRestaurant) {
        EmployeeRestaurantEntity entity = mapper.toEntity(employeeRestaurant);
        repository.save(entity);
    }

    @Override
    public Optional<Long> findRestaurantIdByEmployeeUserId(Long employeeUserId) {
        return repository.findByEmployeeUserId(employeeUserId)
                .map(EmployeeRestaurantEntity::getRestaurantId);
    }

    @Override
    public List<Long> findEmployeeByRestaurantId(Long restaurantId) {
        return repository.findEmployeeIdsByRestaurantId(restaurantId);
    }
}
