package com.pragma.plazoleta.infrastructure.out.jpa.mapper;

import com.pragma.plazoleta.domain.model.DeliveryCode;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DeliveryCodeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IDeliveryCodeEntityMapper {
    DeliveryCode toDomain(DeliveryCodeEntity deliveryCode);
    DeliveryCodeEntity toEntity(DeliveryCode deliveryCode);
}
