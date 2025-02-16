package org.advance.glass.stock.repository;

import org.advance.glass.stock.model.db.ReceiptEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptEntryRepository extends JpaRepository<ReceiptEntry, Long> {

    ReceiptEntry findByReceiptNo(String receiptNo);
}
