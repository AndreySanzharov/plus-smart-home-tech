package ru.yandex.practicum.warehouse.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.general_dto.AddressDto;
import ru.yandex.practicum.warehouse.dto.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse",
        path = "/api/v1/warehouse", fallback = WarehouseServiceFallback.class)
public interface WarehouseServiceClient {
    @PutMapping
    ResponseEntity<Void> addNewProduct(@RequestBody @Valid WarehouseProductDto newProduct);

    @PostMapping("/check")
    ResponseEntity<BookedProductsDto> checkShoppingCart(@RequestBody ShoppingCartDto cart);

    @PostMapping("/add")
    ResponseEntity<Void> addAndChangeQuantityProduct(@RequestBody @Valid AddProductToWarehouseRequest productRequest);

    @GetMapping("/address")
    ResponseEntity<AddressDto> getAddress();

    @PostMapping("/assembly")
    ResponseEntity<BookedProductsDto> prepareOrderItemsForShipment(@RequestBody @Valid AssemblyProductsForOrderRequest request);

    @PostMapping("/return")
    ResponseEntity<Void> returnProductToWarehouse(@RequestBody Map<UUID, Integer> products);

    @PostMapping("/shipped")
    ResponseEntity<Void> sendProductsToDelivery(@RequestBody @Valid ShippedToDeliveryRequest request);
}

