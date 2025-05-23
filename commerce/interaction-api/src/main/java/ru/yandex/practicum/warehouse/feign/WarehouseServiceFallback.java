package ru.yandex.practicum.warehouse.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.exception.ServiceUnavailableException;
import ru.yandex.practicum.general_dto.AddressDto;
import ru.yandex.practicum.warehouse.dto.*;

import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class WarehouseServiceFallback implements WarehouseServiceClient {
    @Override
    public ResponseEntity<Void> addNewProduct(WarehouseProductDto newProduct) {
        log.warn("Активирован резервный вариант для addNewProduct с id: {}", newProduct.getProductId());
        throw new ServiceUnavailableException("Warehouse service недоступен");
    }

    @Override
    public ResponseEntity<BookedProductsDto> checkShoppingCart(ShoppingCartDto cart) {
        log.warn("Активирован резервный вариант для checkShoppingCart с {} товарами", cart.getProducts());
        throw new ServiceUnavailableException("Warehouse service недоступен");
    }

    @Override
    public ResponseEntity<Void> addAndChangeQuantityProduct(AddProductToWarehouseRequest productRequest) {
        log.warn("Активирован резервный вариант для addAndChangeQuantityProduct с id: {}", productRequest.getProductId());
        throw new ServiceUnavailableException("Warehouse service недоступен");
    }

    @Override
    public ResponseEntity<AddressDto> getAddress() {
        log.warn("Активирован резервный вариант для getAddress");
        throw new ServiceUnavailableException("Warehouse service недоступен");
    }

    @Override
    public ResponseEntity<BookedProductsDto> prepareOrderItemsForShipment(AssemblyProductsForOrderRequest request) {
        log.warn("Активирован резервный вариант для prepareOrderItemsForShipment");
        throw new ServiceUnavailableException("Warehouse service недоступен");
    }

    @Override
    public ResponseEntity<Void> returnProductToWarehouse(Map<UUID, Integer> products) {
        log.warn("Активирован резервный вариант для returnProductToWarehouse");
        throw new ServiceUnavailableException("Warehouse service недоступен");
    }

    @Override
    public ResponseEntity<Void> sendProductsToDelivery(ShippedToDeliveryRequest request) {
        log.warn("Активирован резервный вариант для sendProductsToDelivery");
        throw new ServiceUnavailableException("Warehouse service недоступен");
    }
}
