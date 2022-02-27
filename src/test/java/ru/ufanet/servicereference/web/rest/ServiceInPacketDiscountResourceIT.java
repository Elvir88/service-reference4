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
import ru.ufanet.servicereference.domain.ServiceInPacketDiscount;
import ru.ufanet.servicereference.repository.ServiceInPacketDiscountRepository;

/**
 * Integration tests for the {@link ServiceInPacketDiscountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceInPacketDiscountResourceIT {

    private static final Float DEFAULT_COEFFICIENT = 0F;
    private static final Float UPDATED_COEFFICIENT = 1F;

    private static final String ENTITY_API_URL = "/api/service-in-packet-discounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServiceInPacketDiscountRepository serviceInPacketDiscountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceInPacketDiscountMockMvc;

    private ServiceInPacketDiscount serviceInPacketDiscount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceInPacketDiscount createEntity(EntityManager em) {
        ServiceInPacketDiscount serviceInPacketDiscount = new ServiceInPacketDiscount().coefficient(DEFAULT_COEFFICIENT);
        return serviceInPacketDiscount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceInPacketDiscount createUpdatedEntity(EntityManager em) {
        ServiceInPacketDiscount serviceInPacketDiscount = new ServiceInPacketDiscount().coefficient(UPDATED_COEFFICIENT);
        return serviceInPacketDiscount;
    }

    @BeforeEach
    public void initTest() {
        serviceInPacketDiscount = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceInPacketDiscount() throws Exception {
        int databaseSizeBeforeCreate = serviceInPacketDiscountRepository.findAll().size();
        // Create the ServiceInPacketDiscount
        restServiceInPacketDiscountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceInPacketDiscount))
            )
            .andExpect(status().isCreated());

        // Validate the ServiceInPacketDiscount in the database
        List<ServiceInPacketDiscount> serviceInPacketDiscountList = serviceInPacketDiscountRepository.findAll();
        assertThat(serviceInPacketDiscountList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceInPacketDiscount testServiceInPacketDiscount = serviceInPacketDiscountList.get(serviceInPacketDiscountList.size() - 1);
        assertThat(testServiceInPacketDiscount.getCoefficient()).isEqualTo(DEFAULT_COEFFICIENT);
    }

    @Test
    @Transactional
    void createServiceInPacketDiscountWithExistingId() throws Exception {
        // Create the ServiceInPacketDiscount with an existing ID
        serviceInPacketDiscount.setId(1L);

        int databaseSizeBeforeCreate = serviceInPacketDiscountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceInPacketDiscountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceInPacketDiscount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceInPacketDiscount in the database
        List<ServiceInPacketDiscount> serviceInPacketDiscountList = serviceInPacketDiscountRepository.findAll();
        assertThat(serviceInPacketDiscountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCoefficientIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceInPacketDiscountRepository.findAll().size();
        // set the field null
        serviceInPacketDiscount.setCoefficient(null);

        // Create the ServiceInPacketDiscount, which fails.

        restServiceInPacketDiscountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceInPacketDiscount))
            )
            .andExpect(status().isBadRequest());

        List<ServiceInPacketDiscount> serviceInPacketDiscountList = serviceInPacketDiscountRepository.findAll();
        assertThat(serviceInPacketDiscountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceInPacketDiscounts() throws Exception {
        // Initialize the database
        serviceInPacketDiscountRepository.saveAndFlush(serviceInPacketDiscount);

        // Get all the serviceInPacketDiscountList
        restServiceInPacketDiscountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceInPacketDiscount.getId().intValue())))
            .andExpect(jsonPath("$.[*].coefficient").value(hasItem(DEFAULT_COEFFICIENT.doubleValue())));
    }

    @Test
    @Transactional
    void getServiceInPacketDiscount() throws Exception {
        // Initialize the database
        serviceInPacketDiscountRepository.saveAndFlush(serviceInPacketDiscount);

        // Get the serviceInPacketDiscount
        restServiceInPacketDiscountMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceInPacketDiscount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceInPacketDiscount.getId().intValue()))
            .andExpect(jsonPath("$.coefficient").value(DEFAULT_COEFFICIENT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingServiceInPacketDiscount() throws Exception {
        // Get the serviceInPacketDiscount
        restServiceInPacketDiscountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewServiceInPacketDiscount() throws Exception {
        // Initialize the database
        serviceInPacketDiscountRepository.saveAndFlush(serviceInPacketDiscount);

        int databaseSizeBeforeUpdate = serviceInPacketDiscountRepository.findAll().size();

        // Update the serviceInPacketDiscount
        ServiceInPacketDiscount updatedServiceInPacketDiscount = serviceInPacketDiscountRepository
            .findById(serviceInPacketDiscount.getId())
            .get();
        // Disconnect from session so that the updates on updatedServiceInPacketDiscount are not directly saved in db
        em.detach(updatedServiceInPacketDiscount);
        updatedServiceInPacketDiscount.coefficient(UPDATED_COEFFICIENT);

        restServiceInPacketDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceInPacketDiscount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedServiceInPacketDiscount))
            )
            .andExpect(status().isOk());

        // Validate the ServiceInPacketDiscount in the database
        List<ServiceInPacketDiscount> serviceInPacketDiscountList = serviceInPacketDiscountRepository.findAll();
        assertThat(serviceInPacketDiscountList).hasSize(databaseSizeBeforeUpdate);
        ServiceInPacketDiscount testServiceInPacketDiscount = serviceInPacketDiscountList.get(serviceInPacketDiscountList.size() - 1);
        assertThat(testServiceInPacketDiscount.getCoefficient()).isEqualTo(UPDATED_COEFFICIENT);
    }

    @Test
    @Transactional
    void putNonExistingServiceInPacketDiscount() throws Exception {
        int databaseSizeBeforeUpdate = serviceInPacketDiscountRepository.findAll().size();
        serviceInPacketDiscount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceInPacketDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceInPacketDiscount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceInPacketDiscount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceInPacketDiscount in the database
        List<ServiceInPacketDiscount> serviceInPacketDiscountList = serviceInPacketDiscountRepository.findAll();
        assertThat(serviceInPacketDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceInPacketDiscount() throws Exception {
        int databaseSizeBeforeUpdate = serviceInPacketDiscountRepository.findAll().size();
        serviceInPacketDiscount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceInPacketDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceInPacketDiscount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceInPacketDiscount in the database
        List<ServiceInPacketDiscount> serviceInPacketDiscountList = serviceInPacketDiscountRepository.findAll();
        assertThat(serviceInPacketDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceInPacketDiscount() throws Exception {
        int databaseSizeBeforeUpdate = serviceInPacketDiscountRepository.findAll().size();
        serviceInPacketDiscount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceInPacketDiscountMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceInPacketDiscount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceInPacketDiscount in the database
        List<ServiceInPacketDiscount> serviceInPacketDiscountList = serviceInPacketDiscountRepository.findAll();
        assertThat(serviceInPacketDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceInPacketDiscountWithPatch() throws Exception {
        // Initialize the database
        serviceInPacketDiscountRepository.saveAndFlush(serviceInPacketDiscount);

        int databaseSizeBeforeUpdate = serviceInPacketDiscountRepository.findAll().size();

        // Update the serviceInPacketDiscount using partial update
        ServiceInPacketDiscount partialUpdatedServiceInPacketDiscount = new ServiceInPacketDiscount();
        partialUpdatedServiceInPacketDiscount.setId(serviceInPacketDiscount.getId());

        partialUpdatedServiceInPacketDiscount.coefficient(UPDATED_COEFFICIENT);

        restServiceInPacketDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceInPacketDiscount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceInPacketDiscount))
            )
            .andExpect(status().isOk());

        // Validate the ServiceInPacketDiscount in the database
        List<ServiceInPacketDiscount> serviceInPacketDiscountList = serviceInPacketDiscountRepository.findAll();
        assertThat(serviceInPacketDiscountList).hasSize(databaseSizeBeforeUpdate);
        ServiceInPacketDiscount testServiceInPacketDiscount = serviceInPacketDiscountList.get(serviceInPacketDiscountList.size() - 1);
        assertThat(testServiceInPacketDiscount.getCoefficient()).isEqualTo(UPDATED_COEFFICIENT);
    }

    @Test
    @Transactional
    void fullUpdateServiceInPacketDiscountWithPatch() throws Exception {
        // Initialize the database
        serviceInPacketDiscountRepository.saveAndFlush(serviceInPacketDiscount);

        int databaseSizeBeforeUpdate = serviceInPacketDiscountRepository.findAll().size();

        // Update the serviceInPacketDiscount using partial update
        ServiceInPacketDiscount partialUpdatedServiceInPacketDiscount = new ServiceInPacketDiscount();
        partialUpdatedServiceInPacketDiscount.setId(serviceInPacketDiscount.getId());

        partialUpdatedServiceInPacketDiscount.coefficient(UPDATED_COEFFICIENT);

        restServiceInPacketDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceInPacketDiscount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceInPacketDiscount))
            )
            .andExpect(status().isOk());

        // Validate the ServiceInPacketDiscount in the database
        List<ServiceInPacketDiscount> serviceInPacketDiscountList = serviceInPacketDiscountRepository.findAll();
        assertThat(serviceInPacketDiscountList).hasSize(databaseSizeBeforeUpdate);
        ServiceInPacketDiscount testServiceInPacketDiscount = serviceInPacketDiscountList.get(serviceInPacketDiscountList.size() - 1);
        assertThat(testServiceInPacketDiscount.getCoefficient()).isEqualTo(UPDATED_COEFFICIENT);
    }

    @Test
    @Transactional
    void patchNonExistingServiceInPacketDiscount() throws Exception {
        int databaseSizeBeforeUpdate = serviceInPacketDiscountRepository.findAll().size();
        serviceInPacketDiscount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceInPacketDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceInPacketDiscount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceInPacketDiscount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceInPacketDiscount in the database
        List<ServiceInPacketDiscount> serviceInPacketDiscountList = serviceInPacketDiscountRepository.findAll();
        assertThat(serviceInPacketDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceInPacketDiscount() throws Exception {
        int databaseSizeBeforeUpdate = serviceInPacketDiscountRepository.findAll().size();
        serviceInPacketDiscount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceInPacketDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceInPacketDiscount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceInPacketDiscount in the database
        List<ServiceInPacketDiscount> serviceInPacketDiscountList = serviceInPacketDiscountRepository.findAll();
        assertThat(serviceInPacketDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceInPacketDiscount() throws Exception {
        int databaseSizeBeforeUpdate = serviceInPacketDiscountRepository.findAll().size();
        serviceInPacketDiscount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceInPacketDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceInPacketDiscount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceInPacketDiscount in the database
        List<ServiceInPacketDiscount> serviceInPacketDiscountList = serviceInPacketDiscountRepository.findAll();
        assertThat(serviceInPacketDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceInPacketDiscount() throws Exception {
        // Initialize the database
        serviceInPacketDiscountRepository.saveAndFlush(serviceInPacketDiscount);

        int databaseSizeBeforeDelete = serviceInPacketDiscountRepository.findAll().size();

        // Delete the serviceInPacketDiscount
        restServiceInPacketDiscountMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceInPacketDiscount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceInPacketDiscount> serviceInPacketDiscountList = serviceInPacketDiscountRepository.findAll();
        assertThat(serviceInPacketDiscountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
