package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseProductDto {
    @NotNull(message = "id товара должен быть указан")
    UUID productId;
    @NotNull(message = "Поле \"хрупкость\" должно быть указано")
    boolean fragile;
    @NotNull(message = "Размеры должны быть указаны")
    DimensionDto dimension;
    @DecimalMin(value = "1.0", message = "Вес должен быть больше 1")
    double weight;
}
