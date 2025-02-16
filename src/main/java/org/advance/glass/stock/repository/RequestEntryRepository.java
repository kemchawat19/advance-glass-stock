package org.advance.glass.stock.repository;

import org.advance.glass.stock.model.db.RequestEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestEntryRepository extends JpaRepository<RequestEntry, Long> {

    RequestEntry findByRequestNo(String requestNo);
}
