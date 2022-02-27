package ru.ufanet.servicereference.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ufanet.servicereference.domain.ServiceInPacketDiscount;

/**
 * Spring Data SQL repository for the ServiceInPacketDiscount entity.
 */
@Repository
public interface ServiceInPacketDiscountRepository extends JpaRepository<ServiceInPacketDiscount, Long> {
    default Optional<ServiceInPacketDiscount> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ServiceInPacketDiscount> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ServiceInPacketDiscount> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct serviceInPacketDiscount from ServiceInPacketDiscount serviceInPacketDiscount left join fetch serviceInPacketDiscount.service left join fetch serviceInPacketDiscount.tariff left join fetch serviceInPacketDiscount.packetDiscount",
        countQuery = "select count(distinct serviceInPacketDiscount) from ServiceInPacketDiscount serviceInPacketDiscount"
    )
    Page<ServiceInPacketDiscount> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct serviceInPacketDiscount from ServiceInPacketDiscount serviceInPacketDiscount left join fetch serviceInPacketDiscount.service left join fetch serviceInPacketDiscount.tariff left join fetch serviceInPacketDiscount.packetDiscount"
    )
    List<ServiceInPacketDiscount> findAllWithToOneRelationships();

    @Query(
        "select serviceInPacketDiscount from ServiceInPacketDiscount serviceInPacketDiscount left join fetch serviceInPacketDiscount.service left join fetch serviceInPacketDiscount.tariff left join fetch serviceInPacketDiscount.packetDiscount where serviceInPacketDiscount.id =:id"
    )
    Optional<ServiceInPacketDiscount> findOneWithToOneRelationships(@Param("id") Long id);
}
