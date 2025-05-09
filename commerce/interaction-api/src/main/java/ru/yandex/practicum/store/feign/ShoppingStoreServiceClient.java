package ru.yandex.practicum.store.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.store.dto.PageableDto;
import ru.yandex.practicum.store.dto.ProductDto;
import ru.yandex.practicum.store.dto.UpdateQtyStateDto;
import ru.yandex.practicum.store.enums.ProductCategory;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store", fallback = ShoppingStoreServiceFallback.class)
public interface ShoppingStoreServiceClient {

    @GetMapping
    List<ProductDto> getProducts(@RequestParam ProductCategory category,
                                 @SpringQueryMap PageableDto pageableDto);

    @PutMapping
    ProductDto createProduct(@RequestBody ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    Boolean removeProduct(@RequestBody UUID uuid);

    @PostMapping("/quantityState")
    Boolean updateProductQuantityState(@SpringQueryMap UpdateQtyStateDto updateQtyStateDto);

    @GetMapping("/{productId}")
    ProductDto getProductById(@PathVariable("productId") UUID productId);

    @PostMapping("/products/by-ids")
    ResponseEntity<List<ProductDto>> getProductsByIds(@RequestBody Set<UUID> productIds);
}
