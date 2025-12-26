package org.dgika.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Setter
@Getter
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "User.withPrices",
                attributeNodes = @NamedAttributeNode("prices")
        ),
        @NamedEntityGraph(
                name = "User.withPricesAndTicker",
                attributeNodes = @NamedAttributeNode(
                        value = "prices",
                        subgraph = "pricesWithTicker"
                ),
                subgraphs = @NamedSubgraph(
                        name = "pricesWithTicker",
                        attributeNodes = @NamedAttributeNode("ticker")
                )
        )
})
public class User {

    @ManyToMany
    @JoinTable(
            name = "user_prices",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "price_id")
    )
    @ToString.Exclude
    private List<Price> prices;

    @Id
    @Column(name = "id")
    @ToString.Exclude
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "enabled")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (isActive == null) {
            this.isActive = true;
        }
    }
}
