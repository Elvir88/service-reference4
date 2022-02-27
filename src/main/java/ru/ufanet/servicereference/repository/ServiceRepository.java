package ru.ufanet.servicereference.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ufanet.servicereference.domain.Service;

/**
 * Spring Data SQL repository for the Service entity.
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    default Optional<Service> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Service> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Service> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct service from Service service left join fetch service.parent",
        countQuery = "select count(distinct service) from Service service"
    )
    Page<Service> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct service from Service service left join fetch service.parent")
    List<Service> findAllWithToOneRelationships();

    @Query("select service from Service service left join fetch service.parent where service.id =:id")
    Optional<Service> findOneWithToOneRelationships(@Param("id") Long id);
}
