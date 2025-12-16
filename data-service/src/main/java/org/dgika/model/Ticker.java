package org.dgika.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ticker")
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Setter
@Getter
public class Ticker {

    @OneToMany(mappedBy = "ticker")
    @ToString.Exclude
    private List<Price> prices;

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;
}
