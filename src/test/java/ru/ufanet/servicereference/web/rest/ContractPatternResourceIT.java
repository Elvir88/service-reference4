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
import ru.ufanet.servicereference.domain.ContractPattern;
import ru.ufanet.servicereference.repository.ContractPatternRepository;

/**
 * Integration tests for the {@link ContractPatternResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractPatternResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PATTERN_ID = 1;
    private static final Integer UPDATED_PATTERN_ID = 2;

    private static final String ENTITY_API_URL = "/api/contract-patterns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContractPatternRepository contractPatternRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractPatternMockMvc;

    private ContractPattern contractPattern;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractPattern createEntity(EntityManager em) {
        ContractPattern contractPattern = new ContractPattern().title(DEFAULT_TITLE).patternId(DEFAULT_PATTERN_ID);
        return contractPattern;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractPattern createUpdatedEntity(EntityManager em) {
        ContractPattern contractPattern = new ContractPattern().title(UPDATED_TITLE).patternId(UPDATED_PATTERN_ID);
        return contractPattern;
    }

    @BeforeEach
    public void initTest() {
        contractPattern = createEntity(em);
    }

    @Test
    @Transactional
    void createContractPattern() throws Exception {
        int databaseSizeBeforeCreate = contractPatternRepository.findAll().size();
        // Create the ContractPattern
        restContractPatternMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractPattern))
            )
            .andExpect(status().isCreated());

        // Validate the ContractPattern in the database
        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeCreate + 1);
        ContractPattern testContractPattern = contractPatternList.get(contractPatternList.size() - 1);
        assertThat(testContractPattern.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContractPattern.getPatternId()).isEqualTo(DEFAULT_PATTERN_ID);
    }

    @Test
    @Transactional
    void createContractPatternWithExistingId() throws Exception {
        // Create the ContractPattern with an existing ID
        contractPattern.setId(1L);

        int databaseSizeBeforeCreate = contractPatternRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractPatternMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractPattern))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractPattern in the database
        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractPatternRepository.findAll().size();
        // set the field null
        contractPattern.setTitle(null);

        // Create the ContractPattern, which fails.

        restContractPatternMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractPattern))
            )
            .andExpect(status().isBadRequest());

        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPatternIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractPatternRepository.findAll().size();
        // set the field null
        contractPattern.setPatternId(null);

        // Create the ContractPattern, which fails.

        restContractPatternMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractPattern))
            )
            .andExpect(status().isBadRequest());

        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContractPatterns() throws Exception {
        // Initialize the database
        contractPatternRepository.saveAndFlush(contractPattern);

        // Get all the contractPatternList
        restContractPatternMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractPattern.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].patternId").value(hasItem(DEFAULT_PATTERN_ID)));
    }

    @Test
    @Transactional
    void getContractPattern() throws Exception {
        // Initialize the database
        contractPatternRepository.saveAndFlush(contractPattern);

        // Get the contractPattern
        restContractPatternMockMvc
            .perform(get(ENTITY_API_URL_ID, contractPattern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contractPattern.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.patternId").value(DEFAULT_PATTERN_ID));
    }

    @Test
    @Transactional
    void getNonExistingContractPattern() throws Exception {
        // Get the contractPattern
        restContractPatternMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContractPattern() throws Exception {
        // Initialize the database
        contractPatternRepository.saveAndFlush(contractPattern);

        int databaseSizeBeforeUpdate = contractPatternRepository.findAll().size();

        // Update the contractPattern
        ContractPattern updatedContractPattern = contractPatternRepository.findById(contractPattern.getId()).get();
        // Disconnect from session so that the updates on updatedContractPattern are not directly saved in db
        em.detach(updatedContractPattern);
        updatedContractPattern.title(UPDATED_TITLE).patternId(UPDATED_PATTERN_ID);

        restContractPatternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContractPattern.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContractPattern))
            )
            .andExpect(status().isOk());

        // Validate the ContractPattern in the database
        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeUpdate);
        ContractPattern testContractPattern = contractPatternList.get(contractPatternList.size() - 1);
        assertThat(testContractPattern.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContractPattern.getPatternId()).isEqualTo(UPDATED_PATTERN_ID);
    }

    @Test
    @Transactional
    void putNonExistingContractPattern() throws Exception {
        int databaseSizeBeforeUpdate = contractPatternRepository.findAll().size();
        contractPattern.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractPatternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractPattern.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractPattern))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractPattern in the database
        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContractPattern() throws Exception {
        int databaseSizeBeforeUpdate = contractPatternRepository.findAll().size();
        contractPattern.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractPatternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractPattern))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractPattern in the database
        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContractPattern() throws Exception {
        int databaseSizeBeforeUpdate = contractPatternRepository.findAll().size();
        contractPattern.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractPatternMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractPattern))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractPattern in the database
        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractPatternWithPatch() throws Exception {
        // Initialize the database
        contractPatternRepository.saveAndFlush(contractPattern);

        int databaseSizeBeforeUpdate = contractPatternRepository.findAll().size();

        // Update the contractPattern using partial update
        ContractPattern partialUpdatedContractPattern = new ContractPattern();
        partialUpdatedContractPattern.setId(contractPattern.getId());

        partialUpdatedContractPattern.title(UPDATED_TITLE).patternId(UPDATED_PATTERN_ID);

        restContractPatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractPattern.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContractPattern))
            )
            .andExpect(status().isOk());

        // Validate the ContractPattern in the database
        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeUpdate);
        ContractPattern testContractPattern = contractPatternList.get(contractPatternList.size() - 1);
        assertThat(testContractPattern.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContractPattern.getPatternId()).isEqualTo(UPDATED_PATTERN_ID);
    }

    @Test
    @Transactional
    void fullUpdateContractPatternWithPatch() throws Exception {
        // Initialize the database
        contractPatternRepository.saveAndFlush(contractPattern);

        int databaseSizeBeforeUpdate = contractPatternRepository.findAll().size();

        // Update the contractPattern using partial update
        ContractPattern partialUpdatedContractPattern = new ContractPattern();
        partialUpdatedContractPattern.setId(contractPattern.getId());

        partialUpdatedContractPattern.title(UPDATED_TITLE).patternId(UPDATED_PATTERN_ID);

        restContractPatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractPattern.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContractPattern))
            )
            .andExpect(status().isOk());

        // Validate the ContractPattern in the database
        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeUpdate);
        ContractPattern testContractPattern = contractPatternList.get(contractPatternList.size() - 1);
        assertThat(testContractPattern.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContractPattern.getPatternId()).isEqualTo(UPDATED_PATTERN_ID);
    }

    @Test
    @Transactional
    void patchNonExistingContractPattern() throws Exception {
        int databaseSizeBeforeUpdate = contractPatternRepository.findAll().size();
        contractPattern.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractPatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractPattern.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractPattern))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractPattern in the database
        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContractPattern() throws Exception {
        int databaseSizeBeforeUpdate = contractPatternRepository.findAll().size();
        contractPattern.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractPatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractPattern))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractPattern in the database
        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContractPattern() throws Exception {
        int databaseSizeBeforeUpdate = contractPatternRepository.findAll().size();
        contractPattern.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractPatternMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractPattern))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractPattern in the database
        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContractPattern() throws Exception {
        // Initialize the database
        contractPatternRepository.saveAndFlush(contractPattern);

        int databaseSizeBeforeDelete = contractPatternRepository.findAll().size();

        // Delete the contractPattern
        restContractPatternMockMvc
            .perform(delete(ENTITY_API_URL_ID, contractPattern.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContractPattern> contractPatternList = contractPatternRepository.findAll();
        assertThat(contractPatternList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
