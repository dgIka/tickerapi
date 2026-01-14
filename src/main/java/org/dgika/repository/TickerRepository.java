package org.dgika.repository;

import org.dgika.model.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface TickerRepository extends JpaRepository<Ticker, UUID> {
    Optional<Ticker> findByName(String name);


    /**
     * Inserts ticker if it does not exist.
     * If ticker with the same name already exists, does nothing (ON CONFLICT DO NOTHING).
     */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO tickers (id, name)
        VALUES (gen_random_uuid(), :name)
        ON CONFLICT (name) DO NOTHING
        """, nativeQuery = true)
    void insertIgnore(@Param("name") String name);

}
