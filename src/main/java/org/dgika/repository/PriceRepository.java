package org.dgika.repository;

import org.dgika.model.Price;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.Double;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PriceRepository extends CrudRepository<Price, UUID> {

    @Query("select p.date from Price p join p.users u where u.id = :userId and p.ticker.name = :tickerName and p.date between :start and :end")
    List<LocalDate> findExistingDates(UUID userId, String tickerName, LocalDate start, LocalDate end);

    List<Price> findAllByUsers_IdAndTicker_Name(UUID userId, String tickerName);

    @Modifying
    @Transactional
    @Query(value = """
    WITH ins AS (
        INSERT INTO prices (id, ticker_id, date, open, close, high, low)
        VALUES (gen_random_uuid(), :tickerId, :date, :open, :close, :high, :low)
        ON CONFLICT (ticker_id, date) DO NOTHING
        RETURNING id
    )
    INSERT INTO user_prices (user_id, price_id)
    SELECT :userId, id FROM ins
    ON CONFLICT (user_id, price_id) DO NOTHING
    """, nativeQuery = true)
    void insertPrice(UUID userId,
                                 UUID tickerId,
                                 LocalDate date,
                                 Double open,
                                 Double close,
                                 Double high,
                                 Double low);
}
