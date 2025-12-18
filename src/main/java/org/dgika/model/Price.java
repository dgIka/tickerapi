package org.dgika.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "prices")
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Setter
@Getter
public class Price {

    @ManyToMany(mappedBy = "prices")
    @ToString.Exclude
    private List<User> users;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticker_id")
    @ToString.Exclude
    private Ticker ticker;

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "open")
    private Double open;

    @Column(name = "close")
    private Double close;

    @Column(name = "high")
    private Double high;

    @Column(name = "low")
    private Double low;
}
