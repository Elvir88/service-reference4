package ru.ufanet.servicereference.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.ufanet.servicereference.domain.ServiceInPacketDiscount;
import ru.ufanet.servicereference.repository.ServiceInPacketDiscountRepository;
import ru.ufanet.servicereference.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ufanet.servicereference.domain.ServiceInPacketDiscount}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ServiceInPacketDiscountResource {

    private final Logger log = LoggerFactory.getLogger(ServiceInPacketDiscountResource.class);

    private static final String ENTITY_NAME = "serviceInPacketDiscount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceInPacketDiscountRepository serviceInPacketDiscountRepository;

    public ServiceInPacketDiscountResource(ServiceInPacketDiscountRepository serviceInPacketDiscountRepository) {
        this.serviceInPacketDiscountRepository = serviceInPacketDiscountRepository;
    }

    /**
     * {@code POST  /service-in-packet-discounts} : Create a new serviceInPacketDiscount.
     *
     * @param serviceInPacketDiscount the serviceInPacketDiscount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceInPacketDiscount, or with status {@code 400 (Bad Request)} if the serviceInPacketDiscount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-in-packet-discounts")
    public ResponseEntity<ServiceInPacketDiscount> createServiceInPacketDiscount(
        @Valid @RequestBody ServiceInPacketDiscount serviceInPacketDiscount
    ) throws URISyntaxException {
        log.debug("REST request to save ServiceInPacketDiscount : {}", serviceInPacketDiscount);
        if (serviceInPacketDiscount.getId() != null) {
            throw new BadRequestAlertException("A new serviceInPacketDiscount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceInPacketDiscount result = serviceInPacketDiscountRepository.save(serviceInPacketDiscount);
        return ResponseEntity
            .created(new URI("/api/service-in-packet-discounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-in-packet-discounts/:id} : Updates an existing serviceInPacketDiscount.
     *
     * @param id the id of the serviceInPacketDiscount to save.
     * @param serviceInPacketDiscount the serviceInPacketDiscount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceInPacketDiscount,
     * or with status {@code 400 (Bad Request)} if the serviceInPacketDiscount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceInPacketDiscount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-in-packet-discounts/{id}")
    public ResponseEntity<ServiceInPacketDiscount> updateServiceInPacketDiscount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceInPacketDiscount serviceInPacketDiscount
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceInPacketDiscount : {}, {}", id, serviceInPacketDiscount);
        if (serviceInPacketDiscount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceInPacketDiscount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceInPacketDiscountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServiceInPacketDiscount result = serviceInPacketDiscountRepository.save(serviceInPacketDiscount);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceInPacketDiscount.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /service-in-packet-discounts/:id} : Partial updates given fields of an existing serviceInPacketDiscount, field will ignore if it is null
     *
     * @param id the id of the serviceInPacketDiscount to save.
     * @param serviceInPacketDiscount the serviceInPacketDiscount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceInPacketDiscount,
     * or with status {@code 400 (Bad Request)} if the serviceInPacketDiscount is not valid,
     * or with status {@code 404 (Not Found)} if the serviceInPacketDiscount is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceInPacketDiscount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-in-packet-discounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceInPacketDiscount> partialUpdateServiceInPacketDiscount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceInPacketDiscount serviceInPacketDiscount
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceInPacketDiscount partially : {}, {}", id, serviceInPacketDiscount);
        if (serviceInPacketDiscount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceInPacketDiscount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceInPacketDiscountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceInPacketDiscount> result = serviceInPacketDiscountRepository
            .findById(serviceInPacketDiscount.getId())
            .map(existingServiceInPacketDiscount -> {
                if (serviceInPacketDiscount.getCoefficient() != null) {
                    existingServiceInPacketDiscount.setCoefficient(serviceInPacketDiscount.getCoefficient());
                }

                return existingServiceInPacketDiscount;
            })
            .map(serviceInPacketDiscountRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceInPacketDiscount.getId().toString())
        );
    }

    /**
     * {@code GET  /service-in-packet-discounts} : get all the serviceInPacketDiscounts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceInPacketDiscounts in body.
     */
    @GetMapping("/service-in-packet-discounts")
    public List<ServiceInPacketDiscount> getAllServiceInPacketDiscounts(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all ServiceInPacketDiscounts");
        return serviceInPacketDiscountRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /service-in-packet-discounts/:id} : get the "id" serviceInPacketDiscount.
     *
     * @param id the id of the serviceInPacketDiscount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceInPacketDiscount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-in-packet-discounts/{id}")
    public ResponseEntity<ServiceInPacketDiscount> getServiceInPacketDiscount(@PathVariable Long id) {
        log.debug("REST request to get ServiceInPacketDiscount : {}", id);
        Optional<ServiceInPacketDiscount> serviceInPacketDiscount = serviceInPacketDiscountRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(serviceInPacketDiscount);
    }

    /**
     * {@code DELETE  /service-in-packet-discounts/:id} : delete the "id" serviceInPacketDiscount.
     *
     * @param id the id of the serviceInPacketDiscount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-in-packet-discounts/{id}")
    public ResponseEntity<Void> deleteServiceInPacketDiscount(@PathVariable Long id) {
        log.debug("REST request to delete ServiceInPacketDiscount : {}", id);
        serviceInPacketDiscountRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
