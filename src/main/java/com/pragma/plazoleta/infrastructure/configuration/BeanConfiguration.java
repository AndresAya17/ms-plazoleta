package com.pragma.plazoleta.infrastructure.configuration;

import com.pragma.plazoleta.domain.api.IDishServicePort;
import com.pragma.plazoleta.domain.api.IOrderServicePort;
import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.spi.*;
import com.pragma.plazoleta.domain.usecase.DishUseCase;
import com.pragma.plazoleta.domain.usecase.OrderUseCase;
import com.pragma.plazoleta.domain.usecase.RestaurantUseCase;
import com.pragma.plazoleta.infrastructure.out.jpa.adapter.DeliveryCodeJpaAdapter;
import com.pragma.plazoleta.infrastructure.out.jpa.adapter.DishJpaAdapter;
import com.pragma.plazoleta.infrastructure.out.jpa.adapter.OrderJpaAdapter;
import com.pragma.plazoleta.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.*;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IDeliveryCodeRepository;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IDishRepository;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IOrderRepository;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;
    private final IDishPageMapper dishPageMapper;
    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IRestaurantPageMapper restaurantPageMapper;
    private final IDeliveryCodeRepository deliveryCodeRepository;
    private final IDeliveryCodeEntityMapper deliveryCodeEntityMapper;

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantJpaAdapter(
                restaurantRepository,
                restaurantEntityMapper,
                restaurantPageMapper
        );
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(IRestaurantPersistencePort restaurantPersistencePort, IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort) {
        return new RestaurantUseCase(restaurantPersistencePort, employeeRestaurantPersistencePort);
    }

    @Bean
    public IDishPersistencePort dishPersistencePort(){
        return new DishJpaAdapter(dishRepository,dishEntityMapper, dishPageMapper);
    }

    @Bean
    public IDishServicePort dishServicePort(IRestaurantPersistencePort restaurantPersistencePort){
        return new DishUseCase(dishPersistencePort(),restaurantPersistencePort);
    }

    @Bean
    public IOrderPersistencePort orderPersistencePort(){
        return new OrderJpaAdapter(orderRepository,orderEntityMapper);
    }

    @Bean
    public IDeliveryCodePersistencePort deliveryCodePersistencePort(){
        return new DeliveryCodeJpaAdapter(deliveryCodeRepository, deliveryCodeEntityMapper);
    }

    @Bean
    public IOrderServicePort orderServicePort(
            IRestaurantPersistencePort restaurantPersistencePort,
            IDishPersistencePort dishPersistencePort,
            IOrderPersistencePort orderPersistencePort,
            IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort,
            IOrderCodePersistencePort orderCodePersistencePort,
            ISmsPersistencePort smsPersistencePort,
            ICodeGeneratorPort codeGeneratorPort,
            IUserPersistencePort userPersistencePort,
            IDeliveryCodePersistencePort deliveryCodePersistencePort
    ) {

        return new OrderUseCase(
                restaurantPersistencePort,
                dishPersistencePort,
                orderPersistencePort,
                employeeRestaurantPersistencePort,
                orderCodePersistencePort,
                smsPersistencePort,
                codeGeneratorPort,
                userPersistencePort,
                deliveryCodePersistencePort
        );
    }

}