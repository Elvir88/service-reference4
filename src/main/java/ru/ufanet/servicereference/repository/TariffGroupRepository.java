package ru.ufanet.servicereference.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ufanet.servicereference.domain.TariffGroup;

/**
 * Spring Data SQL repository for the TariffGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TariffGroupRepository extends JpaRepository<TariffGroup, Long> {}
