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
import ru.ufanet.servicereference.domain.TariffGroup;
import ru.ufanet.servicereference.repository.TariffGroupRepository;

/**
 * Integration tests for the {@link TariffGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TariffGroupResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tariff-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TariffGroupRepository tariffGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTariffGroupMockMvc;

    private TariffGroup tariffGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TariffGroup createEntity(EntityManager em) {
        TariffGroup tariffGroup = new TariffGroup().title(DEFAULT_TITLE);
        return tariffGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TariffGroup createUpdatedEntity(EntityManager em) {
        TariffGroup tariffGroup = new TariffGroup().title(UPDATED_TITLE);
        return tariffGroup;
    }

    @BeforeEach
    public void initTest() {
        tariffGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createTariffGroup() throws Exception {
        int databaseSizeBeforeCreate = tariffGroupRepository.findAll().size();
        // Create the TariffGroup
        restTariffGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariffGroup)))
            .andExpect(status().isCreated());

        // Validate the TariffGroup in the database
        List<TariffGroup> tariffGroupList = tariffGroupRepository.findAll();
        assertThat(tariffGroupList).hasSize(databaseSizeBeforeCreate + 1);
        TariffGroup testTariffGroup = tariffGroupList.get(tariffGroupList.size() - 1);
        assertThat(testTariffGroup.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void createTariffGroupWithExistingId() throws Exception {
        // Create the TariffGroup with an existing ID
        tariffGroup.setId(1L);

        int databaseSizeBeforeCreate = tariffGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTariffGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariffGroup)))
            .andExpect(status().isBadRequest());

        // Validate the TariffGroup in the database
        List<TariffGroup> tariffGroupList = tariffGroupRepository.findAll();
        assertThat(tariffGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = tariffGroupRepository.findAll().size();
        // set the field null
        tariffGroup.setTitle(null);

        // Create the TariffGroup, which fails.

        restTariffGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariffGroup)))
            .andExpect(status().isBadRequest());

        List<TariffGroup> tariffGroupList = tariffGroupRepository.findAll();
        assertThat(tariffGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTariffGroups() throws Exception {
        // Initialize the database
        tariffGroupRepository.saveAndFlush(tariffGroup);

        // Get all the tariffGroupList
        restTariffGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tariffGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getTariffGroup() throws Exception {
        // Initialize the database
        tariffGroupRepository.saveAndFlush(tariffGroup);

        // Get the tariffGroup
        restTariffGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, tariffGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tariffGroup.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingTariffGroup() throws Exception {
        // Get the tariffGroup
        restTariffGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTariffGroup() throws Exception {
        // Initialize the database
        tariffGroupRepository.saveAndFlush(tariffGroup);

        int databaseSizeBeforeUpdate = tariffGroupRepository.findAll().size();

        // Update the tariffGroup
        TariffGroup updatedTariffGroup = tariffGroupRepository.findById(tariffGroup.getId()).get();
        // Disconnect from session so that the updates on updatedTariffGroup are not directly saved in db
        em.detach(updatedTariffGroup);
        updatedTariffGroup.title(UPDATED_TITLE);

        restTariffGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTariffGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTariffGroup))
            )
            .andExpect(status().isOk());

        // Validate the TariffGroup in the database
        List<TariffGroup> tariffGroupList = tariffGroupRepository.findAll();
        assertThat(tariffGroupList).hasSize(databaseSizeBeforeUpdate);
        TariffGroup testTariffGroup = tariffGroupList.get(tariffGroupList.size() - 1);
        assertThat(testTariffGroup.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingTariffGroup() throws Exception {
        int databaseSizeBeforeUpdate = tariffGroupRepository.findAll().size();
        tariffGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTariffGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tariffGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariffGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the TariffGroup in the database
        List<TariffGroup> tariffGroupList = tariffGroupRepository.findAll();
        assertThat(tariffGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTariffGroup() throws Exception {
        int databaseSizeBeforeUpdate = tariffGroupRepository.findAll().size();
        tariffGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariffGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the TariffGroup in the database
        List<TariffGroup> tariffGroupList = tariffGroupRepository.findAll();
        assertThat(tariffGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTariffGroup() throws Exception {
        int databaseSizeBeforeUpdate = tariffGroupRepository.findAll().size();
        tariffGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariffGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TariffGroup in the database
        List<TariffGroup> tariffGroupList = tariffGroupRepository.findAll();
        assertThat(tariffGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTariffGroupWithPatch() throws Exception {
        // Initialize the database
        tariffGroupRepository.saveAndFlush(tariffGroup);

        int databaseSizeBeforeUpdate = tariffGroupRepository.findAll().size();

        // Update the tariffGroup using partial update
        TariffGroup partialUpdatedTariffGroup = new TariffGroup();
        partialUpdatedTariffGroup.setId(tariffGroup.getId());

        partialUpdatedTariffGroup.title(UPDATED_TITLE);

        restTariffGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTariffGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTariffGroup))
            )
            .andExpect(status().isOk());

        // Validate the TariffGroup in the database
        List<TariffGroup> tariffGroupList = tariffGroupRepository.findAll();
        assertThat(tariffGroupList).hasSize(databaseSizeBeforeUpdate);
        TariffGroup testTariffGroup = tariffGroupList.get(tariffGroupList.size() - 1);
        assertThat(testTariffGroup.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateTariffGroupWithPatch() throws Exception {
        // Initialize the database
        tariffGroupRepository.saveAndFlush(tariffGroup);

        int databaseSizeBeforeUpdate = tariffGroupRepository.findAll().size();

        // Update the tariffGroup using partial update
        TariffGroup partialUpdatedTariffGroup = new TariffGroup();
        partialUpdatedTariffGroup.setId(tariffGroup.getId());

        partialUpdatedTariffGroup.title(UPDATED_TITLE);

        restTariffGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTariffGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTariffGroup))
            )
            .andExpect(status().isOk());

        // Validate the TariffGroup in the database
        List<TariffGroup> tariffGroupList = tariffGroupRepository.findAll();
        assertThat(tariffGroupList).hasSize(databaseSizeBeforeUpdate);
        TariffGroup testTariffGroup = tariffGroupList.get(tariffGroupList.size() - 1);
        assertThat(testTariffGroup.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingTariffGroup() throws Exception {
        int databaseSizeBeforeUpdate = tariffGroupRepository.findAll().size();
        tariffGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTariffGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tariffGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tariffGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the TariffGroup in the database
        List<TariffGroup> tariffGroupList = tariffGroupRepository.findAll();
        assertThat(tariffGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTariffGroup() throws Exception {
        int databaseSizeBeforeUpdate = tariffGroupRepository.findAll().size();
        tariffGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tariffGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the TariffGroup in the database
        List<TariffGroup> tariffGroupList = tariffGroupRepository.findAll();
        assertThat(tariffGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTariffGroup() throws Exception {
        int databaseSizeBeforeUpdate = tariffGroupRepository.findAll().size();
        tariffGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tariffGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TariffGroup in the database
        List<TariffGroup> tariffGroupList = tariffGroupRepository.findAll();
        assertThat(tariffGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTariffGroup() throws Exception {
        // Initialize the database
        tariffGroupRepository.saveAndFlush(tariffGroup);

        int databaseSizeBeforeDelete = tariffGroupRepository.findAll().size();

        // Delete the tariffGroup
        restTariffGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, tariffGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TariffGroup> tariffGroupList = tariffGroupRepository.findAll();
        assertThat(tariffGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
