package org.advance.glass.stock.repository;

import org.advance.glass.stock.model.db.RequestEntryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestEntryDetailRepository extends JpaRepository<RequestEntryDetail, Long> {

}
