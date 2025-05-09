package ru.yandex.practicum.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReturnRequest {
    @NotNull(message = "orderId должен быть указан")
    UUID orderId;
    @NotNull(message = "список товаров должен быть указан")
    Map<UUID, Integer> products;
}
