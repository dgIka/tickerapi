package org.dgika.repository;

import org.dgika.model.Price;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PriceRepository extends CrudRepository<Price, UUID> {

    @Query("select p.date from Price p join p.users u where u.id = :userId and p.ticker.name = :tickerName and p.date between :start and :end")
    List<LocalDate> findExistingDates(UUID userId, String tickerName, LocalDate start, LocalDate end);

    List<Price> findAllByUsers_IdAndTicker_Name(UUID userId, String tickerName);
}
