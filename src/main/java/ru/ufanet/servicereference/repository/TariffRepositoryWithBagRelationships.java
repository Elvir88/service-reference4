package ru.ufanet.servicereference.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import ru.ufanet.servicereference.domain.Tariff;

public interface TariffRepositoryWithBagRelationships {
    Optional<Tariff> fetchBagRelationships(Optional<Tariff> tariff);

    List<Tariff> fetchBagRelationships(List<Tariff> tariffs);

    Page<Tariff> fetchBagRelationships(Page<Tariff> tariffs);
}
