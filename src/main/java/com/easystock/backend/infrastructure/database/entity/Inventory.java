package com.easystock.backend.infrastructure.database.entity;

import com.easystock.backend.infrastructure.database.entity.common.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inventory extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long id;

    private Integer quantity;

    private Integer totalPrice; // 유저가 해당 주식 구매 시 사용한 총 포인트를 의미

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Builder
    public Inventory(Integer quantity, Integer totalPrice, Member member, Stock stock) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.member = member;
        this.stock = stock;
    }

    public void updateQuantity(Integer quantity){
        this.quantity += quantity;
    }
    public void updateTotalPrice(Integer totalPrice){
        this.totalPrice += totalPrice;
    }
}
