package ru.yandex.practicum.service;

import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.general_dto.AddressDto;
import ru.yandex.practicum.warehouse.dto.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void createProduct(WarehouseProductDto newProduct);
    BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto);
    void addAndChangeQuantityProduct(AddProductToWarehouseRequest productRequest);
    AddressDto getAddress();

    BookedProductsDto prepareOrderItemsForShipment(AssemblyProductsForOrderRequest request);

    void returnProductToWarehouse(Map<UUID, Integer> products);

    void sendProductsToDelivery(ShippedToDeliveryRequest request);
}
