package org.dgika.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tickers")
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Setter
@Getter
public class Ticker {

    @OneToMany(mappedBy = "ticker", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Price> prices = new ArrayList<>();

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;
}
