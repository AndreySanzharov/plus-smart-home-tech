package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.payment.enums.PaymentState;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    UUID paymentId;
    @Column(name = "total_payment")
    BigDecimal totalPayment;
    @Column(name = "delivery_total")
    BigDecimal deliveryTotal;
    @Column(name = "fee_total")
    BigDecimal feeTotal;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentState state;
}
