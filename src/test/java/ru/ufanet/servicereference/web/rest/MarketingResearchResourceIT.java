package ru.ufanet.servicereference.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import ru.ufanet.servicereference.domain.MarketingResearch;
import ru.ufanet.servicereference.repository.MarketingResearchRepository;

/**
 * Integration tests for the {@link MarketingResearchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarketingResearchResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/marketing-researches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MarketingResearchRepository marketingResearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarketingResearchMockMvc;

    private MarketingResearch marketingResearch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarketingResearch createEntity(EntityManager em) {
        MarketingResearch marketingResearch = new MarketingResearch().title(DEFAULT_TITLE);
        return marketingResearch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarketingResearch createUpdatedEntity(EntityManager em) {
        MarketingResearch marketingResearch = new MarketingResearch().title(UPDATED_TITLE);
        return marketingResearch;
    }

    @BeforeEach
    public void initTest() {
        marketingResearch = createEntity(em);
    }

    @Test
    @Transactional
    void createMarketingResearch() throws Exception {
        int databaseSizeBeforeCreate = marketingResearchRepository.findAll().size();
        // Create the MarketingResearch
        restMarketingResearchMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marketingResearch))
            )
            .andExpect(status().isCreated());

        // Validate the MarketingResearch in the database
        List<MarketingResearch> marketingResearchList = marketingResearchRepository.findAll();
        assertThat(marketingResearchList).hasSize(databaseSizeBeforeCreate + 1);
        MarketingResearch testMarketingResearch = marketingResearchList.get(marketingResearchList.size() - 1);
        assertThat(testMarketingResearch.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void createMarketingResearchWithExistingId() throws Exception {
        // Create the MarketingResearch with an existing ID
        marketingResearch.setId(1L);

        int databaseSizeBeforeCreate = marketingResearchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarketingResearchMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marketingResearch))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketingResearch in the database
        List<MarketingResearch> marketingResearchList = marketingResearchRepository.findAll();
        assertThat(marketingResearchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = marketingResearchRepository.findAll().size();
        // set the field null
        marketingResearch.setTitle(null);

        // Create the MarketingResearch, which fails.

        restMarketingResearchMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marketingResearch))
            )
            .andExpect(status().isBadRequest());

        List<MarketingResearch> marketingResearchList = marketingResearchRepository.findAll();
        assertThat(marketingResearchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMarketingResearches() throws Exception {
        // Initialize the database
        marketingResearchRepository.saveAndFlush(marketingResearch);

        // Get all the marketingResearchList
        restMarketingResearchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marketingResearch.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getMarketingResearch() throws Exception {
        // Initialize the database
        marketingResearchRepository.saveAndFlush(marketingResearch);

        // Get the marketingResearch
        restMarketingResearchMockMvc
            .perform(get(ENTITY_API_URL_ID, marketingResearch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marketingResearch.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingMarketingResearch() throws Exception {
        // Get the marketingResearch
        restMarketingResearchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMarketingResearch() throws Exception {
        // Initialize the database
        marketingResearchRepository.saveAndFlush(marketingResearch);

        int databaseSizeBeforeUpdate = marketingResearchRepository.findAll().size();

        // Update the marketingResearch
        MarketingResearch updatedMarketingResearch = marketingResearchRepository.findById(marketingResearch.getId()).get();
        // Disconnect from session so that the updates on updatedMarketingResearch are not directly saved in db
        em.detach(updatedMarketingResearch);
        updatedMarketingResearch.title(UPDATED_TITLE);

        restMarketingResearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMarketingResearch.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMarketingResearch))
            )
            .andExpect(status().isOk());

        // Validate the MarketingResearch in the database
        List<MarketingResearch> marketingResearchList = marketingResearchRepository.findAll();
        assertThat(marketingResearchList).hasSize(databaseSizeBeforeUpdate);
        MarketingResearch testMarketingResearch = marketingResearchList.get(marketingResearchList.size() - 1);
        assertThat(testMarketingResearch.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingMarketingResearch() throws Exception {
        int databaseSizeBeforeUpdate = marketingResearchRepository.findAll().size();
        marketingResearch.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarketingResearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marketingResearch.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marketingResearch))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketingResearch in the database
        List<MarketingResearch> marketingResearchList = marketingResearchRepository.findAll();
        assertThat(marketingResearchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMarketingResearch() throws Exception {
        int databaseSizeBeforeUpdate = marketingResearchRepository.findAll().size();
        marketingResearch.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketingResearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marketingResearch))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketingResearch in the database
        List<MarketingResearch> marketingResearchList = marketingResearchRepository.findAll();
        assertThat(marketingResearchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMarketingResearch() throws Exception {
        int databaseSizeBeforeUpdate = marketingResearchRepository.findAll().size();
        marketingResearch.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketingResearchMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marketingResearch))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarketingResearch in the database
        List<MarketingResearch> marketingResearchList = marketingResearchRepository.findAll();
        assertThat(marketingResearchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMarketingResearchWithPatch() throws Exception {
        // Initialize the database
        marketingResearchRepository.saveAndFlush(marketingResearch);

        int databaseSizeBeforeUpdate = marketingResearchRepository.findAll().size();

        // Update the marketingResearch using partial update
        MarketingResearch partialUpdatedMarketingResearch = new MarketingResearch();
        partialUpdatedMarketingResearch.setId(marketingResearch.getId());

        restMarketingResearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarketingResearch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarketingResearch))
            )
            .andExpect(status().isOk());

        // Validate the MarketingResearch in the database
        List<MarketingResearch> marketingResearchList = marketingResearchRepository.findAll();
        assertThat(marketingResearchList).hasSize(databaseSizeBeforeUpdate);
        MarketingResearch testMarketingResearch = marketingResearchList.get(marketingResearchList.size() - 1);
        assertThat(testMarketingResearch.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateMarketingResearchWithPatch() throws Exception {
        // Initialize the database
        marketingResearchRepository.saveAndFlush(marketingResearch);

        int databaseSizeBeforeUpdate = marketingResearchRepository.findAll().size();

        // Update the marketingResearch using partial update
        MarketingResearch partialUpdatedMarketingResearch = new MarketingResearch();
        partialUpdatedMarketingResearch.setId(marketingResearch.getId());

        partialUpdatedMarketingResearch.title(UPDATED_TITLE);

        restMarketingResearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarketingResearch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarketingResearch))
            )
            .andExpect(status().isOk());

        // Validate the MarketingResearch in the database
        List<MarketingResearch> marketingResearchList = marketingResearchRepository.findAll();
        assertThat(marketingResearchList).hasSize(databaseSizeBeforeUpdate);
        MarketingResearch testMarketingResearch = marketingResearchList.get(marketingResearchList.size() - 1);
        assertThat(testMarketingResearch.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingMarketingResearch() throws Exception {
        int databaseSizeBeforeUpdate = marketingResearchRepository.findAll().size();
        marketingResearch.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarketingResearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, marketingResearch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marketingResearch))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketingResearch in the database
        List<MarketingResearch> marketingResearchList = marketingResearchRepository.findAll();
        assertThat(marketingResearchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMarketingResearch() throws Exception {
        int databaseSizeBeforeUpdate = marketingResearchRepository.findAll().size();
        marketingResearch.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketingResearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marketingResearch))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketingResearch in the database
        List<MarketingResearch> marketingResearchList = marketingResearchRepository.findAll();
        assertThat(marketingResearchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMarketingResearch() throws Exception {
        int databaseSizeBeforeUpdate = marketingResearchRepository.findAll().size();
        marketingResearch.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketingResearchMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marketingResearch))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarketingResearch in the database
        List<MarketingResearch> marketingResearchList = marketingResearchRepository.findAll();
        assertThat(marketingResearchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMarketingResearch() throws Exception {
        // Initialize the database
        marketingResearchRepository.saveAndFlush(marketingResearch);

        int databaseSizeBeforeDelete = marketingResearchRepository.findAll().size();

        // Delete the marketingResearch
        restMarketingResearchMockMvc
            .perform(delete(ENTITY_API_URL_ID, marketingResearch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MarketingResearch> marketingResearchList = marketingResearchRepository.findAll();
        assertThat(marketingResearchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
