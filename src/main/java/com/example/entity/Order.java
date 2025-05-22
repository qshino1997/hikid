package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer order_id;

    @Column(name = "user_id", nullable = false)
    private Integer user_id;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal total_amount;

    @Column(name = "status", length = 30, nullable = false)
    private String status;  // NEW, PENDING_PAYMENT, PAID, FAILED…

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "payment_id", length = 64)
    private String payment_id;

    @Column(name = "invoice_number", length = 64)
    private String invoice_number;
}
