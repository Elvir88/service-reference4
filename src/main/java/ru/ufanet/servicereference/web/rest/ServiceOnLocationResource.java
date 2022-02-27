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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ufanet.servicereference.domain.ServiceOnLocation;
import ru.ufanet.servicereference.repository.ServiceOnLocationRepository;
import ru.ufanet.servicereference.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ufanet.servicereference.domain.ServiceOnLocation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ServiceOnLocationResource {

    private final Logger log = LoggerFactory.getLogger(ServiceOnLocationResource.class);

    private static final String ENTITY_NAME = "serviceOnLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceOnLocationRepository serviceOnLocationRepository;

    public ServiceOnLocationResource(ServiceOnLocationRepository serviceOnLocationRepository) {
        this.serviceOnLocationRepository = serviceOnLocationRepository;
    }

    /**
     * {@code POST  /service-on-locations} : Create a new serviceOnLocation.
     *
     * @param serviceOnLocation the serviceOnLocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceOnLocation, or with status {@code 400 (Bad Request)} if the serviceOnLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-on-locations")
    public ResponseEntity<ServiceOnLocation> createServiceOnLocation(@Valid @RequestBody ServiceOnLocation serviceOnLocation)
        throws URISyntaxException {
        log.debug("REST request to save ServiceOnLocation : {}", serviceOnLocation);
        if (serviceOnLocation.getId() != null) {
            throw new BadRequestAlertException("A new serviceOnLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceOnLocation result = serviceOnLocationRepository.save(serviceOnLocation);
        return ResponseEntity
            .created(new URI("/api/service-on-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-on-locations/:id} : Updates an existing serviceOnLocation.
     *
     * @param id the id of the serviceOnLocation to save.
     * @param serviceOnLocation the serviceOnLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceOnLocation,
     * or with status {@code 400 (Bad Request)} if the serviceOnLocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceOnLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-on-locations/{id}")
    public ResponseEntity<ServiceOnLocation> updateServiceOnLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceOnLocation serviceOnLocation
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceOnLocation : {}, {}", id, serviceOnLocation);
        if (serviceOnLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceOnLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceOnLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServiceOnLocation result = serviceOnLocationRepository.save(serviceOnLocation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceOnLocation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /service-on-locations/:id} : Partial updates given fields of an existing serviceOnLocation, field will ignore if it is null
     *
     * @param id the id of the serviceOnLocation to save.
     * @param serviceOnLocation the serviceOnLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceOnLocation,
     * or with status {@code 400 (Bad Request)} if the serviceOnLocation is not valid,
     * or with status {@code 404 (Not Found)} if the serviceOnLocation is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceOnLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-on-locations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceOnLocation> partialUpdateServiceOnLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceOnLocation serviceOnLocation
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceOnLocation partially : {}, {}", id, serviceOnLocation);
        if (serviceOnLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceOnLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceOnLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceOnLocation> result = serviceOnLocationRepository
            .findById(serviceOnLocation.getId())
            .map(existingServiceOnLocation -> {
                if (serviceOnLocation.getDatefrom() != null) {
                    existingServiceOnLocation.setDatefrom(serviceOnLocation.getDatefrom());
                }
                if (serviceOnLocation.getDateTo() != null) {
                    existingServiceOnLocation.setDateTo(serviceOnLocation.getDateTo());
                }

                return existingServiceOnLocation;
            })
            .map(serviceOnLocationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceOnLocation.getId().toString())
        );
    }

    /**
     * {@code GET  /service-on-locations} : get all the serviceOnLocations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceOnLocations in body.
     */
    @GetMapping("/service-on-locations")
    public ResponseEntity<List<ServiceOnLocation>> getAllServiceOnLocations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ServiceOnLocations");
        Page<ServiceOnLocation> page;
        if (eagerload) {
            page = serviceOnLocationRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = serviceOnLocationRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-on-locations/:id} : get the "id" serviceOnLocation.
     *
     * @param id the id of the serviceOnLocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceOnLocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-on-locations/{id}")
    public ResponseEntity<ServiceOnLocation> getServiceOnLocation(@PathVariable Long id) {
        log.debug("REST request to get ServiceOnLocation : {}", id);
        Optional<ServiceOnLocation> serviceOnLocation = serviceOnLocationRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(serviceOnLocation);
    }

    /**
     * {@code DELETE  /service-on-locations/:id} : delete the "id" serviceOnLocation.
     *
     * @param id the id of the serviceOnLocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-on-locations/{id}")
    public ResponseEntity<Void> deleteServiceOnLocation(@PathVariable Long id) {
        log.debug("REST request to delete ServiceOnLocation : {}", id);
        serviceOnLocationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
