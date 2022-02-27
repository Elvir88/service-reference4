package ru.ufanet.servicereference.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ufanet.servicereference.domain.ContractPattern;

/**
 * Spring Data SQL repository for the ContractPattern entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractPatternRepository extends JpaRepository<ContractPattern, Long> {}
