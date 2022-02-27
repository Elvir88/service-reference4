package ru.ufanet.servicereference.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ufanet.servicereference.domain.PromotionOnLocation;

/**
 * Spring Data SQL repository for the PromotionOnLocation entity.
 */
@Repository
public interface PromotionOnLocationRepository extends JpaRepository<PromotionOnLocation, Long> {
    default Optional<PromotionOnLocation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PromotionOnLocation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PromotionOnLocation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct promotionOnLocation from PromotionOnLocation promotionOnLocation left join fetch promotionOnLocation.packetDiscount left join fetch promotionOnLocation.marketingResearch left join fetch promotionOnLocation.location",
        countQuery = "select count(distinct promotionOnLocation) from PromotionOnLocation promotionOnLocation"
    )
    Page<PromotionOnLocation> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct promotionOnLocation from PromotionOnLocation promotionOnLocation left join fetch promotionOnLocation.packetDiscount left join fetch promotionOnLocation.marketingResearch left join fetch promotionOnLocation.location"
    )
    List<PromotionOnLocation> findAllWithToOneRelationships();

    @Query(
        "select promotionOnLocation from PromotionOnLocation promotionOnLocation left join fetch promotionOnLocation.packetDiscount left join fetch promotionOnLocation.marketingResearch left join fetch promotionOnLocation.location where promotionOnLocation.id =:id"
    )
    Optional<PromotionOnLocation> findOneWithToOneRelationships(@Param("id") Long id);
}
