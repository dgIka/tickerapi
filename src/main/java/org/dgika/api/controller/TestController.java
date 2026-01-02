package org.dgika.api.controller;

import org.dgika.api.generated.dto.UserRegisterRequest;
import org.dgika.api.mapper.RegisterMapper;
import org.dgika.model.Price;
import org.dgika.model.Ticker;
import org.dgika.model.User;
import org.dgika.service.PriceService;
import org.dgika.service.TestService;
import org.dgika.service.TickerService;
import org.dgika.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {

    private TickerService tickerService;
    private UserService userService;
    private PriceService priceService;
    private TestService testService;

    @Autowired
    public TestController(TickerService tickerService, UserService userService, PriceService priceService, TestService testService) {
        this.tickerService = tickerService;
        this.userService = userService;
        this.priceService = priceService;
        this.testService = testService;
    }



    @GetMapping("/user/save_prices")
    public void saveUserPrices() {
        testService.createAndAddTestData();
    }

    @GetMapping("/user/saved")
    public void savedUserPrices() {
        List<Price> tickerTest1 = priceService.findAllByUserIdAndTickerName(UUID.fromString("58d40f27-1373-49e4-b8a7-13957fdac217"), "TickerTest1");
        tickerTest1.forEach(System.out::println);
    }


}
