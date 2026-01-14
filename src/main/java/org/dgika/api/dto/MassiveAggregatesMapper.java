package org.dgika.api.dto;

import org.dgika.api.generated.dto.TickerDay;
import org.dgika.massive.dto.MassiveAggregatesResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MassiveAggregatesMapper {
    static public List<TickerDay> toTickerDays(MassiveAggregatesResponse response) {
        if (response == null || response.results() == null || response.results().isEmpty()) {
            return List.of();
        }

        Map<LocalDate, TickerDay> byDate = new TreeMap<>();

        for (MassiveAggregatesResponse.Result r : response.results()) {
            LocalDate date = Instant.ofEpochMilli(r.t())
                    .atZone(ZoneOffset.UTC)
                    .toLocalDate();

            TickerDay day = new TickerDay(date, r.o(), r.c(), r.h(), r.l());
            byDate.put(date, day);
        }

        return new ArrayList<>(byDate.values());
    }
}
