package org.dgika.repository;

import org.dgika.model.Price;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PriceRepository extends CrudRepository<Price, UUID> {

//    List<Price> findByUserId(UUID userId);

}
