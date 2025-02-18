package org.advance.glass.stock.repository;


import org.advance.glass.stock.model.db.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("""
            SELECT stock
            FROM Stock stock
            WHERE stock.product.productCode = :productCode
            """)
    Stock findByProductCode(String productCode);
}