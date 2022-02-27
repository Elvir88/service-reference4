package ru.ufanet.servicereference.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.ufanet.servicereference.domain.Tariff;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TariffRepositoryWithBagRelationshipsImpl implements TariffRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Tariff> fetchBagRelationships(Optional<Tariff> tariff) {
        return tariff.map(this::fetchTariffGroups);
    }

    @Override
    public Page<Tariff> fetchBagRelationships(Page<Tariff> tariffs) {
        return new PageImpl<>(fetchBagRelationships(tariffs.getContent()), tariffs.getPageable(), tariffs.getTotalElements());
    }

    @Override
    public List<Tariff> fetchBagRelationships(List<Tariff> tariffs) {
        return Optional.of(tariffs).map(this::fetchTariffGroups).get();
    }

    Tariff fetchTariffGroups(Tariff result) {
        return entityManager
            .createQuery("select tariff from Tariff tariff left join fetch tariff.tariffGroups where tariff is :tariff", Tariff.class)
            .setParameter("tariff", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Tariff> fetchTariffGroups(List<Tariff> tariffs) {
        return entityManager
            .createQuery(
                "select distinct tariff from Tariff tariff left join fetch tariff.tariffGroups where tariff in :tariffs",
                Tariff.class
            )
            .setParameter("tariffs", tariffs)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
