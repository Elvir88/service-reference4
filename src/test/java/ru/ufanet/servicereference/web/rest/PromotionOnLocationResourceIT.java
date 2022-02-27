package ru.ufanet.servicereference.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.ufanet.servicereference.IntegrationTest;
import ru.ufanet.servicereference.domain.PromotionOnLocation;
import ru.ufanet.servicereference.repository.PromotionOnLocationRepository;

/**
 * Integration tests for the {@link PromotionOnLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PromotionOnLocationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/promotion-on-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PromotionOnLocationRepository promotionOnLocationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPromotionOnLocationMockMvc;

    private PromotionOnLocation promotionOnLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromotionOnLocation createEntity(EntityManager em) {
        PromotionOnLocation promotionOnLocation = new PromotionOnLocation()
            .title(DEFAULT_TITLE)
            .dateFrom(DEFAULT_DATE_FROM)
            .dateTo(DEFAULT_DATE_TO);
        return promotionOnLocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromotionOnLocation createUpdatedEntity(EntityManager em) {
        PromotionOnLocation promotionOnLocation = new PromotionOnLocation()
            .title(UPDATED_TITLE)
            .dateFrom(UPDATED_DATE_FROM)
            .dateTo(UPDATED_DATE_TO);
        return promotionOnLocation;
    }

    @BeforeEach
    public void initTest() {
        promotionOnLocation = createEntity(em);
    }

    @Test
    @Transactional
    void createPromotionOnLocation() throws Exception {
        int databaseSizeBeforeCreate = promotionOnLocationRepository.findAll().size();
        // Create the PromotionOnLocation
        restPromotionOnLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionOnLocation))
            )
            .andExpect(status().isCreated());

        // Validate the PromotionOnLocation in the database
        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeCreate + 1);
        PromotionOnLocation testPromotionOnLocation = promotionOnLocationList.get(promotionOnLocationList.size() - 1);
        assertThat(testPromotionOnLocation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPromotionOnLocation.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testPromotionOnLocation.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
    }

    @Test
    @Transactional
    void createPromotionOnLocationWithExistingId() throws Exception {
        // Create the PromotionOnLocation with an existing ID
        promotionOnLocation.setId(1L);

        int databaseSizeBeforeCreate = promotionOnLocationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromotionOnLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionOnLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromotionOnLocation in the database
        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionOnLocationRepository.findAll().size();
        // set the field null
        promotionOnLocation.setTitle(null);

        // Create the PromotionOnLocation, which fails.

        restPromotionOnLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionOnLocation))
            )
            .andExpect(status().isBadRequest());

        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionOnLocationRepository.findAll().size();
        // set the field null
        promotionOnLocation.setDateFrom(null);

        // Create the PromotionOnLocation, which fails.

        restPromotionOnLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionOnLocation))
            )
            .andExpect(status().isBadRequest());

        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPromotionOnLocations() throws Exception {
        // Initialize the database
        promotionOnLocationRepository.saveAndFlush(promotionOnLocation);

        // Get all the promotionOnLocationList
        restPromotionOnLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promotionOnLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(DEFAULT_DATE_FROM.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(DEFAULT_DATE_TO.toString())));
    }

    @Test
    @Transactional
    void getPromotionOnLocation() throws Exception {
        // Initialize the database
        promotionOnLocationRepository.saveAndFlush(promotionOnLocation);

        // Get the promotionOnLocation
        restPromotionOnLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, promotionOnLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(promotionOnLocation.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.dateFrom").value(DEFAULT_DATE_FROM.toString()))
            .andExpect(jsonPath("$.dateTo").value(DEFAULT_DATE_TO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPromotionOnLocation() throws Exception {
        // Get the promotionOnLocation
        restPromotionOnLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPromotionOnLocation() throws Exception {
        // Initialize the database
        promotionOnLocationRepository.saveAndFlush(promotionOnLocation);

        int databaseSizeBeforeUpdate = promotionOnLocationRepository.findAll().size();

        // Update the promotionOnLocation
        PromotionOnLocation updatedPromotionOnLocation = promotionOnLocationRepository.findById(promotionOnLocation.getId()).get();
        // Disconnect from session so that the updates on updatedPromotionOnLocation are not directly saved in db
        em.detach(updatedPromotionOnLocation);
        updatedPromotionOnLocation.title(UPDATED_TITLE).dateFrom(UPDATED_DATE_FROM).dateTo(UPDATED_DATE_TO);

        restPromotionOnLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPromotionOnLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPromotionOnLocation))
            )
            .andExpect(status().isOk());

        // Validate the PromotionOnLocation in the database
        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeUpdate);
        PromotionOnLocation testPromotionOnLocation = promotionOnLocationList.get(promotionOnLocationList.size() - 1);
        assertThat(testPromotionOnLocation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPromotionOnLocation.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testPromotionOnLocation.getDateTo()).isEqualTo(UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    void putNonExistingPromotionOnLocation() throws Exception {
        int databaseSizeBeforeUpdate = promotionOnLocationRepository.findAll().size();
        promotionOnLocation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromotionOnLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promotionOnLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promotionOnLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromotionOnLocation in the database
        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPromotionOnLocation() throws Exception {
        int databaseSizeBeforeUpdate = promotionOnLocationRepository.findAll().size();
        promotionOnLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionOnLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promotionOnLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromotionOnLocation in the database
        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPromotionOnLocation() throws Exception {
        int databaseSizeBeforeUpdate = promotionOnLocationRepository.findAll().size();
        promotionOnLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionOnLocationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionOnLocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PromotionOnLocation in the database
        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePromotionOnLocationWithPatch() throws Exception {
        // Initialize the database
        promotionOnLocationRepository.saveAndFlush(promotionOnLocation);

        int databaseSizeBeforeUpdate = promotionOnLocationRepository.findAll().size();

        // Update the promotionOnLocation using partial update
        PromotionOnLocation partialUpdatedPromotionOnLocation = new PromotionOnLocation();
        partialUpdatedPromotionOnLocation.setId(promotionOnLocation.getId());

        partialUpdatedPromotionOnLocation.dateFrom(UPDATED_DATE_FROM);

        restPromotionOnLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromotionOnLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromotionOnLocation))
            )
            .andExpect(status().isOk());

        // Validate the PromotionOnLocation in the database
        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeUpdate);
        PromotionOnLocation testPromotionOnLocation = promotionOnLocationList.get(promotionOnLocationList.size() - 1);
        assertThat(testPromotionOnLocation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPromotionOnLocation.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testPromotionOnLocation.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
    }

    @Test
    @Transactional
    void fullUpdatePromotionOnLocationWithPatch() throws Exception {
        // Initialize the database
        promotionOnLocationRepository.saveAndFlush(promotionOnLocation);

        int databaseSizeBeforeUpdate = promotionOnLocationRepository.findAll().size();

        // Update the promotionOnLocation using partial update
        PromotionOnLocation partialUpdatedPromotionOnLocation = new PromotionOnLocation();
        partialUpdatedPromotionOnLocation.setId(promotionOnLocation.getId());

        partialUpdatedPromotionOnLocation.title(UPDATED_TITLE).dateFrom(UPDATED_DATE_FROM).dateTo(UPDATED_DATE_TO);

        restPromotionOnLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromotionOnLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromotionOnLocation))
            )
            .andExpect(status().isOk());

        // Validate the PromotionOnLocation in the database
        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeUpdate);
        PromotionOnLocation testPromotionOnLocation = promotionOnLocationList.get(promotionOnLocationList.size() - 1);
        assertThat(testPromotionOnLocation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPromotionOnLocation.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testPromotionOnLocation.getDateTo()).isEqualTo(UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    void patchNonExistingPromotionOnLocation() throws Exception {
        int databaseSizeBeforeUpdate = promotionOnLocationRepository.findAll().size();
        promotionOnLocation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromotionOnLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, promotionOnLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promotionOnLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromotionOnLocation in the database
        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPromotionOnLocation() throws Exception {
        int databaseSizeBeforeUpdate = promotionOnLocationRepository.findAll().size();
        promotionOnLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionOnLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promotionOnLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromotionOnLocation in the database
        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPromotionOnLocation() throws Exception {
        int databaseSizeBeforeUpdate = promotionOnLocationRepository.findAll().size();
        promotionOnLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionOnLocationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promotionOnLocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PromotionOnLocation in the database
        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePromotionOnLocation() throws Exception {
        // Initialize the database
        promotionOnLocationRepository.saveAndFlush(promotionOnLocation);

        int databaseSizeBeforeDelete = promotionOnLocationRepository.findAll().size();

        // Delete the promotionOnLocation
        restPromotionOnLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, promotionOnLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PromotionOnLocation> promotionOnLocationList = promotionOnLocationRepository.findAll();
        assertThat(promotionOnLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
