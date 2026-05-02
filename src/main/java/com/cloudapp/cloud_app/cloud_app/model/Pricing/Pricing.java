package com.cloudapp.cloud_app.cloud_app.model.Pricing;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pricing_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // type of pricing rule
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PricingRuleType type;

    // value of rule (amount / multiplier)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    // whether rule is active
    private boolean active;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}