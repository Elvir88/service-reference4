package ru.ufanet.servicereference.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ufanet.servicereference.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.ufanet.servicereference.IntegrationTest;
import ru.ufanet.servicereference.domain.Tariff;
import ru.ufanet.servicereference.repository.TariffRepository;

/**
 * Integration tests for the {@link TariffResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TariffResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/tariffs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TariffRepository tariffRepository;

    @Mock
    private TariffRepository tariffRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTariffMockMvc;

    private Tariff tariff;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tariff createEntity(EntityManager em) {
        Tariff tariff = new Tariff().title(DEFAULT_TITLE).cost(DEFAULT_COST);
        return tariff;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tariff createUpdatedEntity(EntityManager em) {
        Tariff tariff = new Tariff().title(UPDATED_TITLE).cost(UPDATED_COST);
        return tariff;
    }

    @BeforeEach
    public void initTest() {
        tariff = createEntity(em);
    }

    @Test
    @Transactional
    void createTariff() throws Exception {
        int databaseSizeBeforeCreate = tariffRepository.findAll().size();
        // Create the Tariff
        restTariffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariff)))
            .andExpect(status().isCreated());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeCreate + 1);
        Tariff testTariff = tariffList.get(tariffList.size() - 1);
        assertThat(testTariff.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTariff.getCost()).isEqualByComparingTo(DEFAULT_COST);
    }

    @Test
    @Transactional
    void createTariffWithExistingId() throws Exception {
        // Create the Tariff with an existing ID
        tariff.setId(1L);

        int databaseSizeBeforeCreate = tariffRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTariffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariff)))
            .andExpect(status().isBadRequest());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = tariffRepository.findAll().size();
        // set the field null
        tariff.setTitle(null);

        // Create the Tariff, which fails.

        restTariffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariff)))
            .andExpect(status().isBadRequest());

        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = tariffRepository.findAll().size();
        // set the field null
        tariff.setCost(null);

        // Create the Tariff, which fails.

        restTariffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariff)))
            .andExpect(status().isBadRequest());

        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTariffs() throws Exception {
        // Initialize the database
        tariffRepository.saveAndFlush(tariff);

        // Get all the tariffList
        restTariffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tariff.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTariffsWithEagerRelationshipsIsEnabled() throws Exception {
        when(tariffRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTariffMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tariffRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTariffsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tariffRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTariffMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tariffRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTariff() throws Exception {
        // Initialize the database
        tariffRepository.saveAndFlush(tariff);

        // Get the tariff
        restTariffMockMvc
            .perform(get(ENTITY_API_URL_ID, tariff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tariff.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.cost").value(sameNumber(DEFAULT_COST)));
    }

    @Test
    @Transactional
    void getNonExistingTariff() throws Exception {
        // Get the tariff
        restTariffMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTariff() throws Exception {
        // Initialize the database
        tariffRepository.saveAndFlush(tariff);

        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();

        // Update the tariff
        Tariff updatedTariff = tariffRepository.findById(tariff.getId()).get();
        // Disconnect from session so that the updates on updatedTariff are not directly saved in db
        em.detach(updatedTariff);
        updatedTariff.title(UPDATED_TITLE).cost(UPDATED_COST);

        restTariffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTariff.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTariff))
            )
            .andExpect(status().isOk());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
        Tariff testTariff = tariffList.get(tariffList.size() - 1);
        assertThat(testTariff.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTariff.getCost()).isEqualByComparingTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void putNonExistingTariff() throws Exception {
        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();
        tariff.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTariffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tariff.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariff))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTariff() throws Exception {
        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();
        tariff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariff))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTariff() throws Exception {
        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();
        tariff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariff)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTariffWithPatch() throws Exception {
        // Initialize the database
        tariffRepository.saveAndFlush(tariff);

        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();

        // Update the tariff using partial update
        Tariff partialUpdatedTariff = new Tariff();
        partialUpdatedTariff.setId(tariff.getId());

        restTariffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTariff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTariff))
            )
            .andExpect(status().isOk());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
        Tariff testTariff = tariffList.get(tariffList.size() - 1);
        assertThat(testTariff.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTariff.getCost()).isEqualByComparingTo(DEFAULT_COST);
    }

    @Test
    @Transactional
    void fullUpdateTariffWithPatch() throws Exception {
        // Initialize the database
        tariffRepository.saveAndFlush(tariff);

        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();

        // Update the tariff using partial update
        Tariff partialUpdatedTariff = new Tariff();
        partialUpdatedTariff.setId(tariff.getId());

        partialUpdatedTariff.title(UPDATED_TITLE).cost(UPDATED_COST);

        restTariffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTariff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTariff))
            )
            .andExpect(status().isOk());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
        Tariff testTariff = tariffList.get(tariffList.size() - 1);
        assertThat(testTariff.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTariff.getCost()).isEqualByComparingTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void patchNonExistingTariff() throws Exception {
        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();
        tariff.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTariffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tariff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tariff))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTariff() throws Exception {
        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();
        tariff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tariff))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTariff() throws Exception {
        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();
        tariff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tariff)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTariff() throws Exception {
        // Initialize the database
        tariffRepository.saveAndFlush(tariff);

        int databaseSizeBeforeDelete = tariffRepository.findAll().size();

        // Delete the tariff
        restTariffMockMvc
            .perform(delete(ENTITY_API_URL_ID, tariff.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
