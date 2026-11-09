package com.fintect.Banking_Engine.modules.fx;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@Entity
@Table(name = "fx_rates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FxRate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 3)
    private String baseCurrency;

    @Column(nullable = false, length = 3)
    private String quoteCurrency;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal rate;

    @Column(nullable = false)
    private Instant lastUpdated;
}