package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "order_bookings")
@Getter
@Setter
@Builder
public class OrderBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID bookingId;
    @ElementCollection
    @CollectionTable(name = "booking_products", joinColumns = @JoinColumn(name = "booking_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Integer> products;
    @Column(name = "order_id")
    UUID orderId;
    @Column(name = "delivery_id")
    UUID deliveryId;
    @Column(name = "fragile")
    private boolean fragile;
    @Column(name = "delivery_volume")
    private double deliveryVolume;
    @Column(name = "delivery_weight")
    private double deliveryWeight;
}
