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
import ru.ufanet.servicereference.domain.House;
import ru.ufanet.servicereference.repository.HouseRepository;
import ru.ufanet.servicereference.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ufanet.servicereference.domain.House}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HouseResource {

    private final Logger log = LoggerFactory.getLogger(HouseResource.class);

    private static final String ENTITY_NAME = "house";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HouseRepository houseRepository;

    public HouseResource(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    /**
     * {@code POST  /houses} : Create a new house.
     *
     * @param house the house to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new house, or with status {@code 400 (Bad Request)} if the house has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/houses")
    public ResponseEntity<House> createHouse(@Valid @RequestBody House house) throws URISyntaxException {
        log.debug("REST request to save House : {}", house);
        if (house.getId() != null) {
            throw new BadRequestAlertException("A new house cannot already have an ID", ENTITY_NAME, "idexists");
        }
        House result = houseRepository.save(house);
        return ResponseEntity
            .created(new URI("/api/houses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /houses/:id} : Updates an existing house.
     *
     * @param id the id of the house to save.
     * @param house the house to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated house,
     * or with status {@code 400 (Bad Request)} if the house is not valid,
     * or with status {@code 500 (Internal Server Error)} if the house couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/houses/{id}")
    public ResponseEntity<House> updateHouse(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody House house)
        throws URISyntaxException {
        log.debug("REST request to update House : {}, {}", id, house);
        if (house.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, house.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!houseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        House result = houseRepository.save(house);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, house.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /houses/:id} : Partial updates given fields of an existing house, field will ignore if it is null
     *
     * @param id the id of the house to save.
     * @param house the house to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated house,
     * or with status {@code 400 (Bad Request)} if the house is not valid,
     * or with status {@code 404 (Not Found)} if the house is not found,
     * or with status {@code 500 (Internal Server Error)} if the house couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/houses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<House> partialUpdateHouse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody House house
    ) throws URISyntaxException {
        log.debug("REST request to partial update House partially : {}, {}", id, house);
        if (house.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, house.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!houseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<House> result = houseRepository
            .findById(house.getId())
            .map(existingHouse -> {
                if (house.getHouseId() != null) {
                    existingHouse.setHouseId(house.getHouseId());
                }

                return existingHouse;
            })
            .map(houseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, house.getId().toString())
        );
    }

    /**
     * {@code GET  /houses} : get all the houses.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of houses in body.
     */
    @GetMapping("/houses")
    public ResponseEntity<List<House>> getAllHouses(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Houses");
        Page<House> page;
        if (eagerload) {
            page = houseRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = houseRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /houses/:id} : get the "id" house.
     *
     * @param id the id of the house to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the house, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/houses/{id}")
    public ResponseEntity<House> getHouse(@PathVariable Long id) {
        log.debug("REST request to get House : {}", id);
        Optional<House> house = houseRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(house);
    }

    /**
     * {@code DELETE  /houses/:id} : delete the "id" house.
     *
     * @param id the id of the house to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/houses/{id}")
    public ResponseEntity<Void> deleteHouse(@PathVariable Long id) {
        log.debug("REST request to delete House : {}", id);
        houseRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
