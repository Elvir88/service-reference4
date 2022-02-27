package ru.ufanet.servicereference.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ufanet.servicereference.domain.ServiceOnLocation;

/**
 * Spring Data SQL repository for the ServiceOnLocation entity.
 */
@Repository
public interface ServiceOnLocationRepository extends JpaRepository<ServiceOnLocation, Long> {
    default Optional<ServiceOnLocation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ServiceOnLocation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ServiceOnLocation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct serviceOnLocation from ServiceOnLocation serviceOnLocation left join fetch serviceOnLocation.service left join fetch serviceOnLocation.tariffGroup left join fetch serviceOnLocation.pattern left join fetch serviceOnLocation.location",
        countQuery = "select count(distinct serviceOnLocation) from ServiceOnLocation serviceOnLocation"
    )
    Page<ServiceOnLocation> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct serviceOnLocation from ServiceOnLocation serviceOnLocation left join fetch serviceOnLocation.service left join fetch serviceOnLocation.tariffGroup left join fetch serviceOnLocation.pattern left join fetch serviceOnLocation.location"
    )
    List<ServiceOnLocation> findAllWithToOneRelationships();

    @Query(
        "select serviceOnLocation from ServiceOnLocation serviceOnLocation left join fetch serviceOnLocation.service left join fetch serviceOnLocation.tariffGroup left join fetch serviceOnLocation.pattern left join fetch serviceOnLocation.location where serviceOnLocation.id =:id"
    )
    Optional<ServiceOnLocation> findOneWithToOneRelationships(@Param("id") Long id);
}
