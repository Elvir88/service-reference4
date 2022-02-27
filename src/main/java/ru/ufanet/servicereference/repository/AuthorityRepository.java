package ru.ufanet.servicereference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ufanet.servicereference.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
