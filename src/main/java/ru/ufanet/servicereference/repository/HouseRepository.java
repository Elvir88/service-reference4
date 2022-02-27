package ru.ufanet.servicereference.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ufanet.servicereference.domain.House;

/**
 * Spring Data SQL repository for the House entity.
 */
@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
    default Optional<House> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<House> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<House> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct house from House house left join fetch house.location",
        countQuery = "select count(distinct house) from House house"
    )
    Page<House> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct house from House house left join fetch house.location")
    List<House> findAllWithToOneRelationships();

    @Query("select house from House house left join fetch house.location where house.id =:id")
    Optional<House> findOneWithToOneRelationships(@Param("id") Long id);
}
