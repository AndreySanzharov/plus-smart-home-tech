package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.delivery.enums.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "deliveries")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    UUID deliveryId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "from_address", nullable = false)
    Address fromAddress;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "to_address", nullable = false)
    Address toAddress;
    @Column(name = "order_id", nullable = false)
    UUID orderId;
    @Column(name = "delivery_state", nullable = false)
    @Enumerated(EnumType.STRING)
    DeliveryState deliveryState;
}
