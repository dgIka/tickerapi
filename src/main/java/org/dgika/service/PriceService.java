package org.dgika.service;

import lombok.RequiredArgsConstructor;
import org.dgika.api.exception.BadRequestException;
import org.dgika.api.generated.dto.TickerDay;
import org.dgika.api.generated.dto.UserSavedResponse;
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
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;
    private final TickerRepository tickerRepository;


    @Transactional
    public Price save(Price price) {
        return priceRepository.save(price);
    }

    @Transactional(readOnly = true)
    public List<LocalDate> findExistingDates(UUID userId, String tickerName, LocalDate start, LocalDate end) {
        return priceRepository.findExistingDates(userId, tickerName, start, end);
    }

    @Transactional(readOnly = true)
    public UserSavedResponse findAllByUserIdAndTickerName(UUID userId, String tickerName) {
        String name = tickerName.toUpperCase();

        tickerRepository.findByName(name)
                .orElseThrow(() -> new BadRequestException("Unknown ticker"));

        List<Price> prices = priceRepository.findAllByUsers_IdAndTicker_Name(userId, name);

        List<TickerDay> result = prices.stream().map(price -> new TickerDay(price.getDate(),
                price.getOpen(),
                price.getClose(),
                price.getHigh(),
                price.getLow())).toList();

        return new UserSavedResponse(userId, name, result);
    }
}
