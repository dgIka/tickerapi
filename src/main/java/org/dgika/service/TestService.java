package org.dgika.service;

import org.dgika.model.Price;
import org.dgika.model.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@Service
public class TestService {

    private final TickerService tickerService;
    private final PriceService priceService;
    private final UserService userService;

    @Autowired
    public TestService(TickerService tickerService, PriceService priceService, UserService userService) {
        this.tickerService = tickerService;
        this.priceService = priceService;
        this.userService = userService;
    }

    @Transactional
    public void createAndAddTestData() {
        Ticker ticker = createTestTicker("TickerTest1");
        tickerService.save(ticker);

        Price price1 = createTestPrice(2022, 1, 1);
        price1.setTicker(ticker);

        Price save = priceService.save(price1);

        Price price2 = createTestPrice(2022, 1, 2);
        price2.setTicker(ticker);

        Price save1 = priceService.save(price2);

        List<Price> prices = Arrays.asList(save1, save);
        userService.saveUserPrices(UUID.fromString("58d40f27-1373-49e4-b8a7-13957fdac217"), prices);
    }

    private Ticker createTestTicker(String name) {
        return Ticker.builder().id(UUID.randomUUID()).name(name).build();
    }

    private Price createTestPrice(int year, int month, int day) {
        return Price.builder().id(UUID.randomUUID()).date(LocalDate.of(year, month, day)).open(1.1).close(5.5).low(1.5).high(3.5).build();
    }


}
