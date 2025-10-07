package org.example.repositories.interfaces;

import org.example.models.Credit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditRepository {

    boolean save(Credit credit);
    List<Credit> findAllPendingCredits();
    Optional<Credit> findById(UUID id);
    boolean validate(UUID id);
}
