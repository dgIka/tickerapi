package org.dgika.service;

import lombok.RequiredArgsConstructor;
import org.dgika.api.dto.MassiveAggregatesMapper;
import org.dgika.api.exception.BadRequestException;
import org.dgika.api.generated.dto.TickerDay;
import org.dgika.api.generated.dto.UserSaveRequest;
import org.dgika.massive.MassiveClient;
import org.dgika.massive.dto.MassiveAggregatesResponse;
import org.dgika.model.Ticker;
import org.dgika.repository.PriceRepository;
import org.dgika.repository.TickerRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TickerService {

    private final TickerRepository tickerRepository;
    private final MassiveClient massiveClient;
    private final PriceRepository priceRepository;

    @Transactional(readOnly = true)
    public Ticker save(Ticker ticker) {
        return tickerRepository.save(ticker);
    }

    @Transactional(readOnly = true)
    public Optional<Ticker> findByName(String name) {
        return tickerRepository.findByName(name);
    }

    @Transactional
    public List<TickerDay> findAndSave(UserSaveRequest usr, UUID userId) {
        if (usr.getStart().isAfter(usr.getEnd())) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        if(usr.getStart().isBefore(LocalDate.now().minusYears(2))) {
            throw new BadRequestException("Start shouldn't be older than 2 years");
        }

        String name = usr.getTicker().toUpperCase();

        tickerRepository.insertIgnore(name);
        Ticker ticker = tickerRepository.findByName(name)
                .orElseThrow(() -> new BadRequestException("Ticker not found"));
        UUID tickerId = ticker.getId();


        MassiveAggregatesResponse dayAggregates = massiveClient.getDayAggregates(
                ticker.getName(),
                usr.getStart(),
                usr.getEnd()
        );

        if (dayAggregates.results() == null || dayAggregates.results().isEmpty()) {
            throw new BadRequestException("Something went wrong");
        }

        List<TickerDay> result = MassiveAggregatesMapper.toTickerDays(dayAggregates);

        result.forEach(r -> priceRepository.insertPrice(
                userId,
                tickerId,
                r.getDate(),
                r.getOpen(),
                r.getClose(),
                r.getHigh(),
                r.getLow()));

        return result;
    }
}
