package org.dgika.service;

import org.dgika.model.Price;
import org.dgika.model.Ticker;
import org.dgika.repository.PriceRepository;
import org.dgika.repository.TickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PriceService {

    private final PriceRepository priceRepository;

    @Autowired
    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Transactional(readOnly = true)
    public Price save(Price price) {
        return priceRepository.save(price);
    }
}
