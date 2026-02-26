package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {
    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant));
        return restaurantEntityMapper.toRestaurant(restaurantEntity);
    }

    @Override
    public Optional<Restaurant> findById(Long idRestaurant) {
        return restaurantRepository.findById(idRestaurant)
                .map(restaurantEntityMapper::toRestaurant);

    }

    @Override
    public PageResult<Restaurant> listRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<RestaurantEntity> pageResult =
                restaurantRepository.findAllByOrderByNameAsc(pageable);

        List<Restaurant> restaurants =
                pageResult.getContent()
                        .stream()
                        .map(restaurantEntityMapper::toRestaurant)
                        .toList();

        return new PageResult<>(
                restaurants,
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements()
        );
    }
}
