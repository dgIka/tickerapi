package org.dgika.massive;

import lombok.RequiredArgsConstructor;
import org.dgika.massive.dto.MassiveAggregatesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpHeaders;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MassiveClient {

    private final MassiveProperties props;
    private final RestClient massiveRestClient;

    public MassiveAggregatesResponse getDayAggregates(String ticker, LocalDate start, LocalDate end) {
        return massiveRestClient.get().uri(uriBuilder -> uriBuilder
                .path("/v2/aggs/ticker/{ticker}/range/1/day/{start}/{end}")
                .build(ticker, start, end)
        ).header(HttpHeaders.AUTHORIZATION, "Bearer " + props.key())
                .retrieve()
                .body(MassiveAggregatesResponse.class);
    }

}
