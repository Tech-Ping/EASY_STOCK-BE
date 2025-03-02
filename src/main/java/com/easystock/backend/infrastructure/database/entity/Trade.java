package com.easystock.backend.infrastructure.database.entity;

import com.easystock.backend.infrastructure.database.entity.common.AuditingEntity;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.infrastructure.database.entity.enums.TradeType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trade extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    @Enumerated(EnumType.STRING)
    private TradeType type;

    private Integer quantity;

    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Member customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Builder
    public Trade(TradeStatus status, TradeType type, Integer quantity, Integer price, Member customer, Stock stock) {
        this.status = status;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.customer = customer;
        this.stock = stock;
    }

    public void cancelTrade(){
        this.status = TradeStatus.CANCELED;
    }

    public void completeTrade(){
        this.status = TradeStatus.COMPLETED;
    }

}
