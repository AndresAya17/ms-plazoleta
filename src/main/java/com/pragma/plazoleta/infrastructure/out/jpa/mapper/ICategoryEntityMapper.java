package com.pragma.plazoleta.infrastructure.out.jpa.mapper;

import com.pragma.plazoleta.domain.model.Category;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ICategoryEntityMapper {

    default CategoryEntity toEntity(Long id) {
        if (id == null) {
            return null;
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setId(id);
        return entity;
    }
    
    CategoryEntity toEntity(Category category);

    Category toDomain(CategoryEntity categoryEntity);
}
