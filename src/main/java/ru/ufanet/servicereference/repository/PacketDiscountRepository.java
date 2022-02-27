package ru.ufanet.servicereference.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.ufanet.servicereference.domain.PacketDiscount;

/**
 * Spring Data SQL repository for the PacketDiscount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PacketDiscountRepository extends JpaRepository<PacketDiscount, Long> {}
