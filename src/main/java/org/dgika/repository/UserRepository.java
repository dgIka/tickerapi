package org.dgika.repository;

import org.dgika.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(value = "User.withPricesAndTicker",
            type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findById(UUID id);

    Optional<User> findByName(String name);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
