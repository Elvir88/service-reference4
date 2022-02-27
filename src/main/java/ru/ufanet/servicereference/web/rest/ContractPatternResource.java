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
import ru.ufanet.servicereference.domain.ContractPattern;
import ru.ufanet.servicereference.repository.ContractPatternRepository;
import ru.ufanet.servicereference.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ufanet.servicereference.domain.ContractPattern}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContractPatternResource {

    private final Logger log = LoggerFactory.getLogger(ContractPatternResource.class);

    private static final String ENTITY_NAME = "contractPattern";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractPatternRepository contractPatternRepository;

    public ContractPatternResource(ContractPatternRepository contractPatternRepository) {
        this.contractPatternRepository = contractPatternRepository;
    }

    /**
     * {@code POST  /contract-patterns} : Create a new contractPattern.
     *
     * @param contractPattern the contractPattern to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractPattern, or with status {@code 400 (Bad Request)} if the contractPattern has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contract-patterns")
    public ResponseEntity<ContractPattern> createContractPattern(@Valid @RequestBody ContractPattern contractPattern)
        throws URISyntaxException {
        log.debug("REST request to save ContractPattern : {}", contractPattern);
        if (contractPattern.getId() != null) {
            throw new BadRequestAlertException("A new contractPattern cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContractPattern result = contractPatternRepository.save(contractPattern);
        return ResponseEntity
            .created(new URI("/api/contract-patterns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contract-patterns/:id} : Updates an existing contractPattern.
     *
     * @param id the id of the contractPattern to save.
     * @param contractPattern the contractPattern to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractPattern,
     * or with status {@code 400 (Bad Request)} if the contractPattern is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractPattern couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contract-patterns/{id}")
    public ResponseEntity<ContractPattern> updateContractPattern(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContractPattern contractPattern
    ) throws URISyntaxException {
        log.debug("REST request to update ContractPattern : {}, {}", id, contractPattern);
        if (contractPattern.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractPattern.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractPatternRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContractPattern result = contractPatternRepository.save(contractPattern);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractPattern.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contract-patterns/:id} : Partial updates given fields of an existing contractPattern, field will ignore if it is null
     *
     * @param id the id of the contractPattern to save.
     * @param contractPattern the contractPattern to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractPattern,
     * or with status {@code 400 (Bad Request)} if the contractPattern is not valid,
     * or with status {@code 404 (Not Found)} if the contractPattern is not found,
     * or with status {@code 500 (Internal Server Error)} if the contractPattern couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contract-patterns/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContractPattern> partialUpdateContractPattern(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContractPattern contractPattern
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContractPattern partially : {}, {}", id, contractPattern);
        if (contractPattern.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractPattern.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractPatternRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContractPattern> result = contractPatternRepository
            .findById(contractPattern.getId())
            .map(existingContractPattern -> {
                if (contractPattern.getTitle() != null) {
                    existingContractPattern.setTitle(contractPattern.getTitle());
                }
                if (contractPattern.getPatternId() != null) {
                    existingContractPattern.setPatternId(contractPattern.getPatternId());
                }

                return existingContractPattern;
            })
            .map(contractPatternRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractPattern.getId().toString())
        );
    }

    /**
     * {@code GET  /contract-patterns} : get all the contractPatterns.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contractPatterns in body.
     */
    @GetMapping("/contract-patterns")
    public ResponseEntity<List<ContractPattern>> getAllContractPatterns(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ContractPatterns");
        Page<ContractPattern> page = contractPatternRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contract-patterns/:id} : get the "id" contractPattern.
     *
     * @param id the id of the contractPattern to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractPattern, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contract-patterns/{id}")
    public ResponseEntity<ContractPattern> getContractPattern(@PathVariable Long id) {
        log.debug("REST request to get ContractPattern : {}", id);
        Optional<ContractPattern> contractPattern = contractPatternRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contractPattern);
    }

    /**
     * {@code DELETE  /contract-patterns/:id} : delete the "id" contractPattern.
     *
     * @param id the id of the contractPattern to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contract-patterns/{id}")
    public ResponseEntity<Void> deleteContractPattern(@PathVariable Long id) {
        log.debug("REST request to delete ContractPattern : {}", id);
        contractPatternRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
