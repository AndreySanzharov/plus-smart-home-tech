package ru.yandex.practicum.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.store.dto.ProductDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);

    List<ProductDto> toDtoList(List<Product> list);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromDto(ProductDto productDto, @MappingTarget Product product);
}
