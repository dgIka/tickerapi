package org.dgika.service;

import lombok.RequiredArgsConstructor;
import org.dgika.api.exception.BadRequestException;
import org.dgika.api.generated.dto.UserSaveRequest;
import org.dgika.massive.MassiveClient;
import org.dgika.massive.dto.MassiveAggregatesResponse;
import org.dgika.model.Ticker;
import org.dgika.repository.TickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TickerService {

    private final TickerRepository tickerRepository;
    private final MassiveClient massiveClient;

    @Transactional(readOnly = true)
    public Ticker save(Ticker ticker) {
        return tickerRepository.save(ticker);
    }

    @Transactional(readOnly = true)
    public Ticker findByName(String name) {
        return tickerRepository.findByName(name);
    }

    @Transactional
    public List<Ticker> findAndSave(UserSaveRequest usr) {
        if (usr.getStart().isAfter(usr.getEnd())) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        if(usr.getStart().isBefore(LocalDate.now().minusYears(2))) {
            throw new BadRequestException("Start shouldn't be older than 2 years");
        }

        MassiveAggregatesResponse dayAggregates = massiveClient.getDayAggregates(
                usr.getTicker().toUpperCase(),
                usr.getStart(),
                usr.getEnd()
        );

        if (dayAggregates.results().isEmpty()) {
            throw new BadRequestException("Something went wrong");
        }
        return null;
    }
}
