package com.easystock.backend.infrastructure.database.repository;

import com.easystock.backend.infrastructure.database.entity.Inventory;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.entity.Trade;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByMemberAndStock(Member member, Stock stock);

    @Query("SELECT COALESCE(i.quantity, 0) FROM Inventory i WHERE i.member = :member AND i.stock = :stock")
    int findStockQuantityByMemberAndStock(@Param("member") Member member, @Param("stock") Stock stock);

<<<<<<< HEAD
    List<Inventory> findAllByMember(Member member);
=======
    Optional<List<Inventory>> findByMemberId(Long memberId);

>>>>>>> d93513166a63cc6e39300d2834dce644eea9205e
}
