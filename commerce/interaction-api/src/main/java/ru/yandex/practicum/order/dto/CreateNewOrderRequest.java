package ru.yandex.practicum.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.general_dto.AddressDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateNewOrderRequest {
    @NotNull(message = "корзина должна быть указана")
    ShoppingCartDto shoppingCartDto;
    @NotNull(message = "адрес должен быть указан")
    @Valid
    AddressDto addressDto;
}
