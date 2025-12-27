package org.dgika.repository;

import org.dgika.model.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TickerRepository extends JpaRepository<Ticker, UUID> {
    Ticker findByName(String name);


}
