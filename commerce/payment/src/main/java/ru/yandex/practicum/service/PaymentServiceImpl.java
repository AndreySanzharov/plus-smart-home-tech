package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.IncompleteProductListException;
import ru.yandex.practicum.exception.NoPaymentFoundException;
import ru.yandex.practicum.exception.StoreServiceReturnedNullException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.feign.OrderServiceClient;
import ru.yandex.practicum.payment.dto.PaymentDto;
import ru.yandex.practicum.payment.enums.PaymentState;
import ru.yandex.practicum.repository.PaymentRepository;
import ru.yandex.practicum.store.dto.ProductDto;
import ru.yandex.practicum.store.feign.ShoppingStoreServiceClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;
    ShoppingStoreServiceClient shoppingStoreServiceClient;
    OrderServiceClient orderServiceClient;

    static BigDecimal TAX_RATE = BigDecimal.valueOf(10.0);

    @Override
    @Transactional
    public PaymentDto createPayment(OrderDto orderDto) {
        log.info("Создание платежного документа для заказа {}", orderDto);
        BigDecimal feeTotal = orderDto.getProductPrice()
                .multiply(TAX_RATE)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        Payment payment = Payment.builder()
                .deliveryTotal(orderDto.getDeliveryPrice())
                .feeTotal(feeTotal)
                .totalPayment(calculateTotalCost(orderDto))
                .build();
        log.info("Сохранение платежного документа {}", payment);
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    public BigDecimal calculateTotalCost(OrderDto orderDto) {
        BigDecimal productPrice = orderDto.getProductPrice();
        BigDecimal taxRate = BigDecimal.valueOf(1.1);
        BigDecimal productPriceWithTax = productPrice
                .multiply(taxRate)
                .setScale(2, RoundingMode.HALF_UP);
        return productPriceWithTax.add(orderDto.getDeliveryPrice());
    }

    @Override
    @Transactional
    public void refundPayment(UUID paymentId) {
        log.info("Проведение оплаты для платежа с id {}", paymentId);
        Payment payment = getPaymentById(paymentId);
        payment.setState(PaymentState.SUCCESS);
        log.info("Отправление в сервис заказов уведомления об оплате для платежа {}", payment);
        orderServiceClient.paymentOrder(paymentId);
        log.info("Сохранение успешного платежа в базу {}", payment);
        paymentRepository.save(payment);
    }


    @Override
    public BigDecimal calculateProductCost(OrderDto orderDto) {
        Map<UUID, Integer> orderProducts = orderDto.getProducts();
        Set<UUID> productIds = orderDto.getProducts().keySet();
        List<ProductDto> listProducts = Optional.ofNullable(shoppingStoreServiceClient.getProductsByIds(productIds).getBody())
                .orElseThrow(() -> new StoreServiceReturnedNullException("Не удалось получить список товаров"));
        if (productIds.size() != listProducts.size()) {
            throw new IncompleteProductListException("Количество товаров не соответствует количеству запрошенных ids");
        }
        return listProducts.stream()
                .map(product -> {
                    int quantity = orderProducts.get(product.getProductId());
                    return product.getPrice().multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void failPayment(UUID paymentId) {
        log.info("Неудачная оплата для платежа с id {}", paymentId);
        Payment payment = getPaymentById(paymentId);
        payment.setState(PaymentState.FAILED);
        log.info("Отправление в сервис заказов уведомления об отказе оплаты для платежа {}", payment);
        orderServiceClient.paymentOrderFailed(paymentId);
        log.info("Сохранение неудачного платежа в базу {}", payment);
        paymentRepository.save(payment);
    }

    private Payment getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException("Платеж не найден id" + paymentId));
    }
}
