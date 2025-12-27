package org.dgika.service;

import org.dgika.model.Price;
import org.dgika.model.Ticker;
import org.dgika.repository.PriceRepository;
import org.dgika.repository.TickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PriceService {

    private final PriceRepository priceRepository;

    @Autowired
    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Transactional
    public Price save(Price price) {
        return priceRepository.save(price);
    }

    @Transactional(readOnly = true)
    public List<LocalDate> findExistingDates(UUID userId, String tickerName, LocalDate start, LocalDate end) {
        return priceRepository.findExistingDates(userId, tickerName, start, end);
    }

    @Transactional(readOnly = true)
    public List<Price> findAllByUserIdAndTickerName(UUID userId, String tickerName) {
        return priceRepository.findAllByUsers_IdAndTicker_Name(userId, tickerName);
    }
}
