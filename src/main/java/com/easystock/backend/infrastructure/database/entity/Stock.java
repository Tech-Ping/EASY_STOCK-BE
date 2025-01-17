package com.easystock.backend.infrastructure.database.entity;

import com.easystock.backend.infrastructure.database.entity.enums.MarketType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MarketType type;

    private String info;

    private String imgUrl;
}
