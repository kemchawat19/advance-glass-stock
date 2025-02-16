package org.advance.glass.stock.repository;

import org.advance.glass.stock.model.db.ReceiptEntryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptEntryDetailRepository extends JpaRepository<ReceiptEntryDetail, Long> {

}
