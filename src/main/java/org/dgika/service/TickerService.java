package org.dgika.service;

import org.dgika.model.Ticker;
import org.dgika.repository.TickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TickerService {

    private final TickerRepository tickerRepository;

    @Autowired
    public TickerService(TickerRepository tickerRepository) {
        this.tickerRepository = tickerRepository;
    }

    @Transactional(readOnly = true)
    public Ticker save(Ticker ticker) {
        return tickerRepository.save(ticker);
    }
    @Transactional(readOnly = true)
    public Ticker findByName(String name) {
        return tickerRepository.findByName(name);
    }
}
