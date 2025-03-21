package org.advance.glass.stock.repository;

import org.advance.glass.stock.model.db.EntryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryDetailRepository extends JpaRepository<EntryDetail, Long> {

}
