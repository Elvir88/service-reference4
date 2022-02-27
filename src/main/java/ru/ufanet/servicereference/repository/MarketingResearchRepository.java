package ru.ufanet.servicereference.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ufanet.servicereference.domain.MarketingResearch;

/**
 * Spring Data SQL repository for the MarketingResearch entity.
 */
@Repository
public interface MarketingResearchRepository extends JpaRepository<MarketingResearch, Long> {
    default Optional<MarketingResearch> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MarketingResearch> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MarketingResearch> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct marketingResearch from MarketingResearch marketingResearch left join fetch marketingResearch.service left join fetch marketingResearch.tariff",
        countQuery = "select count(distinct marketingResearch) from MarketingResearch marketingResearch"
    )
    Page<MarketingResearch> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct marketingResearch from MarketingResearch marketingResearch left join fetch marketingResearch.service left join fetch marketingResearch.tariff"
    )
    List<MarketingResearch> findAllWithToOneRelationships();

    @Query(
        "select marketingResearch from MarketingResearch marketingResearch left join fetch marketingResearch.service left join fetch marketingResearch.tariff where marketingResearch.id =:id"
    )
    Optional<MarketingResearch> findOneWithToOneRelationships(@Param("id") Long id);
}
