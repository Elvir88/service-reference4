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
import ru.ufanet.servicereference.domain.House;
import ru.ufanet.servicereference.repository.HouseRepository;

/**
 * Integration tests for the {@link HouseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HouseResourceIT {

    private static final Integer DEFAULT_HOUSE_ID = 1;
    private static final Integer UPDATED_HOUSE_ID = 2;

    private static final String ENTITY_API_URL = "/api/houses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHouseMockMvc;

    private House house;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static House createEntity(EntityManager em) {
        House house = new House().houseId(DEFAULT_HOUSE_ID);
        return house;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static House createUpdatedEntity(EntityManager em) {
        House house = new House().houseId(UPDATED_HOUSE_ID);
        return house;
    }

    @BeforeEach
    public void initTest() {
        house = createEntity(em);
    }

    @Test
    @Transactional
    void createHouse() throws Exception {
        int databaseSizeBeforeCreate = houseRepository.findAll().size();
        // Create the House
        restHouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(house)))
            .andExpect(status().isCreated());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeCreate + 1);
        House testHouse = houseList.get(houseList.size() - 1);
        assertThat(testHouse.getHouseId()).isEqualTo(DEFAULT_HOUSE_ID);
    }

    @Test
    @Transactional
    void createHouseWithExistingId() throws Exception {
        // Create the House with an existing ID
        house.setId(1L);

        int databaseSizeBeforeCreate = houseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(house)))
            .andExpect(status().isBadRequest());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHouseIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = houseRepository.findAll().size();
        // set the field null
        house.setHouseId(null);

        // Create the House, which fails.

        restHouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(house)))
            .andExpect(status().isBadRequest());

        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHouses() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList
        restHouseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(house.getId().intValue())))
            .andExpect(jsonPath("$.[*].houseId").value(hasItem(DEFAULT_HOUSE_ID)));
    }

    @Test
    @Transactional
    void getHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get the house
        restHouseMockMvc
            .perform(get(ENTITY_API_URL_ID, house.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(house.getId().intValue()))
            .andExpect(jsonPath("$.houseId").value(DEFAULT_HOUSE_ID));
    }

    @Test
    @Transactional
    void getNonExistingHouse() throws Exception {
        // Get the house
        restHouseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        int databaseSizeBeforeUpdate = houseRepository.findAll().size();

        // Update the house
        House updatedHouse = houseRepository.findById(house.getId()).get();
        // Disconnect from session so that the updates on updatedHouse are not directly saved in db
        em.detach(updatedHouse);
        updatedHouse.houseId(UPDATED_HOUSE_ID);

        restHouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHouse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHouse))
            )
            .andExpect(status().isOk());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeUpdate);
        House testHouse = houseList.get(houseList.size() - 1);
        assertThat(testHouse.getHouseId()).isEqualTo(UPDATED_HOUSE_ID);
    }

    @Test
    @Transactional
    void putNonExistingHouse() throws Exception {
        int databaseSizeBeforeUpdate = houseRepository.findAll().size();
        house.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, house.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(house))
            )
            .andExpect(status().isBadRequest());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHouse() throws Exception {
        int databaseSizeBeforeUpdate = houseRepository.findAll().size();
        house.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(house))
            )
            .andExpect(status().isBadRequest());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHouse() throws Exception {
        int databaseSizeBeforeUpdate = houseRepository.findAll().size();
        house.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(house)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHouseWithPatch() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        int databaseSizeBeforeUpdate = houseRepository.findAll().size();

        // Update the house using partial update
        House partialUpdatedHouse = new House();
        partialUpdatedHouse.setId(house.getId());

        partialUpdatedHouse.houseId(UPDATED_HOUSE_ID);

        restHouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHouse))
            )
            .andExpect(status().isOk());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeUpdate);
        House testHouse = houseList.get(houseList.size() - 1);
        assertThat(testHouse.getHouseId()).isEqualTo(UPDATED_HOUSE_ID);
    }

    @Test
    @Transactional
    void fullUpdateHouseWithPatch() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        int databaseSizeBeforeUpdate = houseRepository.findAll().size();

        // Update the house using partial update
        House partialUpdatedHouse = new House();
        partialUpdatedHouse.setId(house.getId());

        partialUpdatedHouse.houseId(UPDATED_HOUSE_ID);

        restHouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHouse))
            )
            .andExpect(status().isOk());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeUpdate);
        House testHouse = houseList.get(houseList.size() - 1);
        assertThat(testHouse.getHouseId()).isEqualTo(UPDATED_HOUSE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingHouse() throws Exception {
        int databaseSizeBeforeUpdate = houseRepository.findAll().size();
        house.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, house.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(house))
            )
            .andExpect(status().isBadRequest());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHouse() throws Exception {
        int databaseSizeBeforeUpdate = houseRepository.findAll().size();
        house.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(house))
            )
            .andExpect(status().isBadRequest());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHouse() throws Exception {
        int databaseSizeBeforeUpdate = houseRepository.findAll().size();
        house.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(house)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        int databaseSizeBeforeDelete = houseRepository.findAll().size();

        // Delete the house
        restHouseMockMvc
            .perform(delete(ENTITY_API_URL_ID, house.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
