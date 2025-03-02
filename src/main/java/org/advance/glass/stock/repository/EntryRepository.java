package org.advance.glass.stock.repository;

import org.advance.glass.stock.model.db.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

    @Query(value = """
            SELECT nextval(:sequenceName)
            """, nativeQuery = true)
    BigInteger getNextSequenceValue(@Param("sequenceName") String sequenceName);

    Entry findByEntryNumber(String entryNumber);
}
