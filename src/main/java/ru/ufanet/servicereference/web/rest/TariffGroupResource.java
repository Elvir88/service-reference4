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
import ru.ufanet.servicereference.domain.TariffGroup;
import ru.ufanet.servicereference.repository.TariffGroupRepository;
import ru.ufanet.servicereference.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ufanet.servicereference.domain.TariffGroup}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TariffGroupResource {

    private final Logger log = LoggerFactory.getLogger(TariffGroupResource.class);

    private static final String ENTITY_NAME = "tariffGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TariffGroupRepository tariffGroupRepository;

    public TariffGroupResource(TariffGroupRepository tariffGroupRepository) {
        this.tariffGroupRepository = tariffGroupRepository;
    }

    /**
     * {@code POST  /tariff-groups} : Create a new tariffGroup.
     *
     * @param tariffGroup the tariffGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tariffGroup, or with status {@code 400 (Bad Request)} if the tariffGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tariff-groups")
    public ResponseEntity<TariffGroup> createTariffGroup(@Valid @RequestBody TariffGroup tariffGroup) throws URISyntaxException {
        log.debug("REST request to save TariffGroup : {}", tariffGroup);
        if (tariffGroup.getId() != null) {
            throw new BadRequestAlertException("A new tariffGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TariffGroup result = tariffGroupRepository.save(tariffGroup);
        return ResponseEntity
            .created(new URI("/api/tariff-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tariff-groups/:id} : Updates an existing tariffGroup.
     *
     * @param id the id of the tariffGroup to save.
     * @param tariffGroup the tariffGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tariffGroup,
     * or with status {@code 400 (Bad Request)} if the tariffGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tariffGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tariff-groups/{id}")
    public ResponseEntity<TariffGroup> updateTariffGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TariffGroup tariffGroup
    ) throws URISyntaxException {
        log.debug("REST request to update TariffGroup : {}, {}", id, tariffGroup);
        if (tariffGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tariffGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tariffGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TariffGroup result = tariffGroupRepository.save(tariffGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tariffGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tariff-groups/:id} : Partial updates given fields of an existing tariffGroup, field will ignore if it is null
     *
     * @param id the id of the tariffGroup to save.
     * @param tariffGroup the tariffGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tariffGroup,
     * or with status {@code 400 (Bad Request)} if the tariffGroup is not valid,
     * or with status {@code 404 (Not Found)} if the tariffGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the tariffGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tariff-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TariffGroup> partialUpdateTariffGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TariffGroup tariffGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update TariffGroup partially : {}, {}", id, tariffGroup);
        if (tariffGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tariffGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tariffGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TariffGroup> result = tariffGroupRepository
            .findById(tariffGroup.getId())
            .map(existingTariffGroup -> {
                if (tariffGroup.getTitle() != null) {
                    existingTariffGroup.setTitle(tariffGroup.getTitle());
                }

                return existingTariffGroup;
            })
            .map(tariffGroupRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tariffGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /tariff-groups} : get all the tariffGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tariffGroups in body.
     */
    @GetMapping("/tariff-groups")
    public ResponseEntity<List<TariffGroup>> getAllTariffGroups(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TariffGroups");
        Page<TariffGroup> page = tariffGroupRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tariff-groups/:id} : get the "id" tariffGroup.
     *
     * @param id the id of the tariffGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tariffGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tariff-groups/{id}")
    public ResponseEntity<TariffGroup> getTariffGroup(@PathVariable Long id) {
        log.debug("REST request to get TariffGroup : {}", id);
        Optional<TariffGroup> tariffGroup = tariffGroupRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tariffGroup);
    }

    /**
     * {@code DELETE  /tariff-groups/:id} : delete the "id" tariffGroup.
     *
     * @param id the id of the tariffGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tariff-groups/{id}")
    public ResponseEntity<Void> deleteTariffGroup(@PathVariable Long id) {
        log.debug("REST request to delete TariffGroup : {}", id);
        tariffGroupRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
