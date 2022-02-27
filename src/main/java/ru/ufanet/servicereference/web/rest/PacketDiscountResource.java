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
import ru.ufanet.servicereference.domain.PacketDiscount;
import ru.ufanet.servicereference.repository.PacketDiscountRepository;
import ru.ufanet.servicereference.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.ufanet.servicereference.domain.PacketDiscount}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PacketDiscountResource {

    private final Logger log = LoggerFactory.getLogger(PacketDiscountResource.class);

    private static final String ENTITY_NAME = "packetDiscount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PacketDiscountRepository packetDiscountRepository;

    public PacketDiscountResource(PacketDiscountRepository packetDiscountRepository) {
        this.packetDiscountRepository = packetDiscountRepository;
    }

    /**
     * {@code POST  /packet-discounts} : Create a new packetDiscount.
     *
     * @param packetDiscount the packetDiscount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new packetDiscount, or with status {@code 400 (Bad Request)} if the packetDiscount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/packet-discounts")
    public ResponseEntity<PacketDiscount> createPacketDiscount(@Valid @RequestBody PacketDiscount packetDiscount)
        throws URISyntaxException {
        log.debug("REST request to save PacketDiscount : {}", packetDiscount);
        if (packetDiscount.getId() != null) {
            throw new BadRequestAlertException("A new packetDiscount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PacketDiscount result = packetDiscountRepository.save(packetDiscount);
        return ResponseEntity
            .created(new URI("/api/packet-discounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /packet-discounts/:id} : Updates an existing packetDiscount.
     *
     * @param id the id of the packetDiscount to save.
     * @param packetDiscount the packetDiscount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packetDiscount,
     * or with status {@code 400 (Bad Request)} if the packetDiscount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the packetDiscount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/packet-discounts/{id}")
    public ResponseEntity<PacketDiscount> updatePacketDiscount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PacketDiscount packetDiscount
    ) throws URISyntaxException {
        log.debug("REST request to update PacketDiscount : {}, {}", id, packetDiscount);
        if (packetDiscount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packetDiscount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packetDiscountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PacketDiscount result = packetDiscountRepository.save(packetDiscount);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packetDiscount.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /packet-discounts/:id} : Partial updates given fields of an existing packetDiscount, field will ignore if it is null
     *
     * @param id the id of the packetDiscount to save.
     * @param packetDiscount the packetDiscount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packetDiscount,
     * or with status {@code 400 (Bad Request)} if the packetDiscount is not valid,
     * or with status {@code 404 (Not Found)} if the packetDiscount is not found,
     * or with status {@code 500 (Internal Server Error)} if the packetDiscount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/packet-discounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PacketDiscount> partialUpdatePacketDiscount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PacketDiscount packetDiscount
    ) throws URISyntaxException {
        log.debug("REST request to partial update PacketDiscount partially : {}, {}", id, packetDiscount);
        if (packetDiscount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packetDiscount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packetDiscountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PacketDiscount> result = packetDiscountRepository
            .findById(packetDiscount.getId())
            .map(existingPacketDiscount -> {
                if (packetDiscount.getTitle() != null) {
                    existingPacketDiscount.setTitle(packetDiscount.getTitle());
                }
                if (packetDiscount.getCost() != null) {
                    existingPacketDiscount.setCost(packetDiscount.getCost());
                }

                return existingPacketDiscount;
            })
            .map(packetDiscountRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packetDiscount.getId().toString())
        );
    }

    /**
     * {@code GET  /packet-discounts} : get all the packetDiscounts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packetDiscounts in body.
     */
    @GetMapping("/packet-discounts")
    public ResponseEntity<List<PacketDiscount>> getAllPacketDiscounts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PacketDiscounts");
        Page<PacketDiscount> page = packetDiscountRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /packet-discounts/:id} : get the "id" packetDiscount.
     *
     * @param id the id of the packetDiscount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the packetDiscount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/packet-discounts/{id}")
    public ResponseEntity<PacketDiscount> getPacketDiscount(@PathVariable Long id) {
        log.debug("REST request to get PacketDiscount : {}", id);
        Optional<PacketDiscount> packetDiscount = packetDiscountRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(packetDiscount);
    }

    /**
     * {@code DELETE  /packet-discounts/:id} : delete the "id" packetDiscount.
     *
     * @param id the id of the packetDiscount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/packet-discounts/{id}")
    public ResponseEntity<Void> deletePacketDiscount(@PathVariable Long id) {
        log.debug("REST request to delete PacketDiscount : {}", id);
        packetDiscountRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
