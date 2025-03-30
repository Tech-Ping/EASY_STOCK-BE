package com.easystock.backend.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Entity
@Builder
@RequiredArgsConstructor @AllArgsConstructor
@Table(name = "stock_record")
public class StockRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String stockCode;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer closePrice;
}
