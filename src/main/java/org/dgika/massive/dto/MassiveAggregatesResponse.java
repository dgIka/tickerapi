package org.dgika.massive.dto;

import java.util.List;

public record MassiveAggregatesResponse(
        List<Result> results
) {
    public record Result(
            long t,
            double o,
            double c,
            double h,
            double l
    ) {}
}
