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
import ru.ufanet.servicereference.domain.MarketingResearch;
import ru.ufanet.servicereference.repository.MarketingResearchRepository;
import ru.ufanet.servicereference.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ufanet.servicereference.domain.MarketingResearch}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MarketingResearchResource {

    private final Logger log = LoggerFactory.getLogger(MarketingResearchResource.class);

    private static final String ENTITY_NAME = "marketingResearch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarketingResearchRepository marketingResearchRepository;

    public MarketingResearchResource(MarketingResearchRepository marketingResearchRepository) {
        this.marketingResearchRepository = marketingResearchRepository;
    }

    /**
     * {@code POST  /marketing-researches} : Create a new marketingResearch.
     *
     * @param marketingResearch the marketingResearch to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marketingResearch, or with status {@code 400 (Bad Request)} if the marketingResearch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/marketing-researches")
    public ResponseEntity<MarketingResearch> createMarketingResearch(@Valid @RequestBody MarketingResearch marketingResearch)
        throws URISyntaxException {
        log.debug("REST request to save MarketingResearch : {}", marketingResearch);
        if (marketingResearch.getId() != null) {
            throw new BadRequestAlertException("A new marketingResearch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MarketingResearch result = marketingResearchRepository.save(marketingResearch);
        return ResponseEntity
            .created(new URI("/api/marketing-researches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /marketing-researches/:id} : Updates an existing marketingResearch.
     *
     * @param id the id of the marketingResearch to save.
     * @param marketingResearch the marketingResearch to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketingResearch,
     * or with status {@code 400 (Bad Request)} if the marketingResearch is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marketingResearch couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/marketing-researches/{id}")
    public ResponseEntity<MarketingResearch> updateMarketingResearch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MarketingResearch marketingResearch
    ) throws URISyntaxException {
        log.debug("REST request to update MarketingResearch : {}, {}", id, marketingResearch);
        if (marketingResearch.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketingResearch.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marketingResearchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MarketingResearch result = marketingResearchRepository.save(marketingResearch);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marketingResearch.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /marketing-researches/:id} : Partial updates given fields of an existing marketingResearch, field will ignore if it is null
     *
     * @param id the id of the marketingResearch to save.
     * @param marketingResearch the marketingResearch to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketingResearch,
     * or with status {@code 400 (Bad Request)} if the marketingResearch is not valid,
     * or with status {@code 404 (Not Found)} if the marketingResearch is not found,
     * or with status {@code 500 (Internal Server Error)} if the marketingResearch couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/marketing-researches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MarketingResearch> partialUpdateMarketingResearch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MarketingResearch marketingResearch
    ) throws URISyntaxException {
        log.debug("REST request to partial update MarketingResearch partially : {}, {}", id, marketingResearch);
        if (marketingResearch.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketingResearch.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marketingResearchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MarketingResearch> result = marketingResearchRepository
            .findById(marketingResearch.getId())
            .map(existingMarketingResearch -> {
                if (marketingResearch.getTitle() != null) {
                    existingMarketingResearch.setTitle(marketingResearch.getTitle());
                }

                return existingMarketingResearch;
            })
            .map(marketingResearchRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marketingResearch.getId().toString())
        );
    }

    /**
     * {@code GET  /marketing-researches} : get all the marketingResearches.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marketingResearches in body.
     */
    @GetMapping("/marketing-researches")
    public ResponseEntity<List<MarketingResearch>> getAllMarketingResearches(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of MarketingResearches");
        Page<MarketingResearch> page;
        if (eagerload) {
            page = marketingResearchRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = marketingResearchRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /marketing-researches/:id} : get the "id" marketingResearch.
     *
     * @param id the id of the marketingResearch to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marketingResearch, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/marketing-researches/{id}")
    public ResponseEntity<MarketingResearch> getMarketingResearch(@PathVariable Long id) {
        log.debug("REST request to get MarketingResearch : {}", id);
        Optional<MarketingResearch> marketingResearch = marketingResearchRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(marketingResearch);
    }

    /**
     * {@code DELETE  /marketing-researches/:id} : delete the "id" marketingResearch.
     *
     * @param id the id of the marketingResearch to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/marketing-researches/{id}")
    public ResponseEntity<Void> deleteMarketingResearch(@PathVariable Long id) {
        log.debug("REST request to delete MarketingResearch : {}", id);
        marketingResearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
