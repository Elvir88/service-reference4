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
import ru.ufanet.servicereference.domain.ServiceOnLocation;
import ru.ufanet.servicereference.repository.ServiceOnLocationRepository;

/**
 * Integration tests for the {@link ServiceOnLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceOnLocationResourceIT {

    private static final Instant DEFAULT_DATEFROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATEFROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/service-on-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServiceOnLocationRepository serviceOnLocationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceOnLocationMockMvc;

    private ServiceOnLocation serviceOnLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceOnLocation createEntity(EntityManager em) {
        ServiceOnLocation serviceOnLocation = new ServiceOnLocation().datefrom(DEFAULT_DATEFROM).dateTo(DEFAULT_DATE_TO);
        return serviceOnLocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceOnLocation createUpdatedEntity(EntityManager em) {
        ServiceOnLocation serviceOnLocation = new ServiceOnLocation().datefrom(UPDATED_DATEFROM).dateTo(UPDATED_DATE_TO);
        return serviceOnLocation;
    }

    @BeforeEach
    public void initTest() {
        serviceOnLocation = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceOnLocation() throws Exception {
        int databaseSizeBeforeCreate = serviceOnLocationRepository.findAll().size();
        // Create the ServiceOnLocation
        restServiceOnLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceOnLocation))
            )
            .andExpect(status().isCreated());

        // Validate the ServiceOnLocation in the database
        List<ServiceOnLocation> serviceOnLocationList = serviceOnLocationRepository.findAll();
        assertThat(serviceOnLocationList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceOnLocation testServiceOnLocation = serviceOnLocationList.get(serviceOnLocationList.size() - 1);
        assertThat(testServiceOnLocation.getDatefrom()).isEqualTo(DEFAULT_DATEFROM);
        assertThat(testServiceOnLocation.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
    }

    @Test
    @Transactional
    void createServiceOnLocationWithExistingId() throws Exception {
        // Create the ServiceOnLocation with an existing ID
        serviceOnLocation.setId(1L);

        int databaseSizeBeforeCreate = serviceOnLocationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceOnLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceOnLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOnLocation in the database
        List<ServiceOnLocation> serviceOnLocationList = serviceOnLocationRepository.findAll();
        assertThat(serviceOnLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDatefromIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceOnLocationRepository.findAll().size();
        // set the field null
        serviceOnLocation.setDatefrom(null);

        // Create the ServiceOnLocation, which fails.

        restServiceOnLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceOnLocation))
            )
            .andExpect(status().isBadRequest());

        List<ServiceOnLocation> serviceOnLocationList = serviceOnLocationRepository.findAll();
        assertThat(serviceOnLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceOnLocations() throws Exception {
        // Initialize the database
        serviceOnLocationRepository.saveAndFlush(serviceOnLocation);

        // Get all the serviceOnLocationList
        restServiceOnLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOnLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].datefrom").value(hasItem(DEFAULT_DATEFROM.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(DEFAULT_DATE_TO.toString())));
    }

    @Test
    @Transactional
    void getServiceOnLocation() throws Exception {
        // Initialize the database
        serviceOnLocationRepository.saveAndFlush(serviceOnLocation);

        // Get the serviceOnLocation
        restServiceOnLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceOnLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceOnLocation.getId().intValue()))
            .andExpect(jsonPath("$.datefrom").value(DEFAULT_DATEFROM.toString()))
            .andExpect(jsonPath("$.dateTo").value(DEFAULT_DATE_TO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingServiceOnLocation() throws Exception {
        // Get the serviceOnLocation
        restServiceOnLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewServiceOnLocation() throws Exception {
        // Initialize the database
        serviceOnLocationRepository.saveAndFlush(serviceOnLocation);

        int databaseSizeBeforeUpdate = serviceOnLocationRepository.findAll().size();

        // Update the serviceOnLocation
        ServiceOnLocation updatedServiceOnLocation = serviceOnLocationRepository.findById(serviceOnLocation.getId()).get();
        // Disconnect from session so that the updates on updatedServiceOnLocation are not directly saved in db
        em.detach(updatedServiceOnLocation);
        updatedServiceOnLocation.datefrom(UPDATED_DATEFROM).dateTo(UPDATED_DATE_TO);

        restServiceOnLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceOnLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedServiceOnLocation))
            )
            .andExpect(status().isOk());

        // Validate the ServiceOnLocation in the database
        List<ServiceOnLocation> serviceOnLocationList = serviceOnLocationRepository.findAll();
        assertThat(serviceOnLocationList).hasSize(databaseSizeBeforeUpdate);
        ServiceOnLocation testServiceOnLocation = serviceOnLocationList.get(serviceOnLocationList.size() - 1);
        assertThat(testServiceOnLocation.getDatefrom()).isEqualTo(UPDATED_DATEFROM);
        assertThat(testServiceOnLocation.getDateTo()).isEqualTo(UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    void putNonExistingServiceOnLocation() throws Exception {
        int databaseSizeBeforeUpdate = serviceOnLocationRepository.findAll().size();
        serviceOnLocation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceOnLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceOnLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceOnLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOnLocation in the database
        List<ServiceOnLocation> serviceOnLocationList = serviceOnLocationRepository.findAll();
        assertThat(serviceOnLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceOnLocation() throws Exception {
        int databaseSizeBeforeUpdate = serviceOnLocationRepository.findAll().size();
        serviceOnLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOnLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceOnLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOnLocation in the database
        List<ServiceOnLocation> serviceOnLocationList = serviceOnLocationRepository.findAll();
        assertThat(serviceOnLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceOnLocation() throws Exception {
        int databaseSizeBeforeUpdate = serviceOnLocationRepository.findAll().size();
        serviceOnLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOnLocationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceOnLocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceOnLocation in the database
        List<ServiceOnLocation> serviceOnLocationList = serviceOnLocationRepository.findAll();
        assertThat(serviceOnLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceOnLocationWithPatch() throws Exception {
        // Initialize the database
        serviceOnLocationRepository.saveAndFlush(serviceOnLocation);

        int databaseSizeBeforeUpdate = serviceOnLocationRepository.findAll().size();

        // Update the serviceOnLocation using partial update
        ServiceOnLocation partialUpdatedServiceOnLocation = new ServiceOnLocation();
        partialUpdatedServiceOnLocation.setId(serviceOnLocation.getId());

        partialUpdatedServiceOnLocation.datefrom(UPDATED_DATEFROM).dateTo(UPDATED_DATE_TO);

        restServiceOnLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceOnLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceOnLocation))
            )
            .andExpect(status().isOk());

        // Validate the ServiceOnLocation in the database
        List<ServiceOnLocation> serviceOnLocationList = serviceOnLocationRepository.findAll();
        assertThat(serviceOnLocationList).hasSize(databaseSizeBeforeUpdate);
        ServiceOnLocation testServiceOnLocation = serviceOnLocationList.get(serviceOnLocationList.size() - 1);
        assertThat(testServiceOnLocation.getDatefrom()).isEqualTo(UPDATED_DATEFROM);
        assertThat(testServiceOnLocation.getDateTo()).isEqualTo(UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    void fullUpdateServiceOnLocationWithPatch() throws Exception {
        // Initialize the database
        serviceOnLocationRepository.saveAndFlush(serviceOnLocation);

        int databaseSizeBeforeUpdate = serviceOnLocationRepository.findAll().size();

        // Update the serviceOnLocation using partial update
        ServiceOnLocation partialUpdatedServiceOnLocation = new ServiceOnLocation();
        partialUpdatedServiceOnLocation.setId(serviceOnLocation.getId());

        partialUpdatedServiceOnLocation.datefrom(UPDATED_DATEFROM).dateTo(UPDATED_DATE_TO);

        restServiceOnLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceOnLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceOnLocation))
            )
            .andExpect(status().isOk());

        // Validate the ServiceOnLocation in the database
        List<ServiceOnLocation> serviceOnLocationList = serviceOnLocationRepository.findAll();
        assertThat(serviceOnLocationList).hasSize(databaseSizeBeforeUpdate);
        ServiceOnLocation testServiceOnLocation = serviceOnLocationList.get(serviceOnLocationList.size() - 1);
        assertThat(testServiceOnLocation.getDatefrom()).isEqualTo(UPDATED_DATEFROM);
        assertThat(testServiceOnLocation.getDateTo()).isEqualTo(UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    void patchNonExistingServiceOnLocation() throws Exception {
        int databaseSizeBeforeUpdate = serviceOnLocationRepository.findAll().size();
        serviceOnLocation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceOnLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceOnLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceOnLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOnLocation in the database
        List<ServiceOnLocation> serviceOnLocationList = serviceOnLocationRepository.findAll();
        assertThat(serviceOnLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceOnLocation() throws Exception {
        int databaseSizeBeforeUpdate = serviceOnLocationRepository.findAll().size();
        serviceOnLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOnLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceOnLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOnLocation in the database
        List<ServiceOnLocation> serviceOnLocationList = serviceOnLocationRepository.findAll();
        assertThat(serviceOnLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceOnLocation() throws Exception {
        int databaseSizeBeforeUpdate = serviceOnLocationRepository.findAll().size();
        serviceOnLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOnLocationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceOnLocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceOnLocation in the database
        List<ServiceOnLocation> serviceOnLocationList = serviceOnLocationRepository.findAll();
        assertThat(serviceOnLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceOnLocation() throws Exception {
        // Initialize the database
        serviceOnLocationRepository.saveAndFlush(serviceOnLocation);

        int databaseSizeBeforeDelete = serviceOnLocationRepository.findAll().size();

        // Delete the serviceOnLocation
        restServiceOnLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceOnLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceOnLocation> serviceOnLocationList = serviceOnLocationRepository.findAll();
        assertThat(serviceOnLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
