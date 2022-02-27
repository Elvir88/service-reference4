package ru.ufanet.servicereference.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.ufanet.servicereference.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
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
import ru.ufanet.servicereference.domain.PacketDiscount;
import ru.ufanet.servicereference.repository.PacketDiscountRepository;

/**
 * Integration tests for the {@link PacketDiscountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PacketDiscountResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/packet-discounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PacketDiscountRepository packetDiscountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPacketDiscountMockMvc;

    private PacketDiscount packetDiscount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PacketDiscount createEntity(EntityManager em) {
        PacketDiscount packetDiscount = new PacketDiscount().title(DEFAULT_TITLE).cost(DEFAULT_COST);
        return packetDiscount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PacketDiscount createUpdatedEntity(EntityManager em) {
        PacketDiscount packetDiscount = new PacketDiscount().title(UPDATED_TITLE).cost(UPDATED_COST);
        return packetDiscount;
    }

    @BeforeEach
    public void initTest() {
        packetDiscount = createEntity(em);
    }

    @Test
    @Transactional
    void createPacketDiscount() throws Exception {
        int databaseSizeBeforeCreate = packetDiscountRepository.findAll().size();
        // Create the PacketDiscount
        restPacketDiscountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packetDiscount))
            )
            .andExpect(status().isCreated());

        // Validate the PacketDiscount in the database
        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeCreate + 1);
        PacketDiscount testPacketDiscount = packetDiscountList.get(packetDiscountList.size() - 1);
        assertThat(testPacketDiscount.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPacketDiscount.getCost()).isEqualByComparingTo(DEFAULT_COST);
    }

    @Test
    @Transactional
    void createPacketDiscountWithExistingId() throws Exception {
        // Create the PacketDiscount with an existing ID
        packetDiscount.setId(1L);

        int databaseSizeBeforeCreate = packetDiscountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacketDiscountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packetDiscount))
            )
            .andExpect(status().isBadRequest());

        // Validate the PacketDiscount in the database
        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = packetDiscountRepository.findAll().size();
        // set the field null
        packetDiscount.setTitle(null);

        // Create the PacketDiscount, which fails.

        restPacketDiscountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packetDiscount))
            )
            .andExpect(status().isBadRequest());

        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = packetDiscountRepository.findAll().size();
        // set the field null
        packetDiscount.setCost(null);

        // Create the PacketDiscount, which fails.

        restPacketDiscountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packetDiscount))
            )
            .andExpect(status().isBadRequest());

        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPacketDiscounts() throws Exception {
        // Initialize the database
        packetDiscountRepository.saveAndFlush(packetDiscount);

        // Get all the packetDiscountList
        restPacketDiscountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packetDiscount.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))));
    }

    @Test
    @Transactional
    void getPacketDiscount() throws Exception {
        // Initialize the database
        packetDiscountRepository.saveAndFlush(packetDiscount);

        // Get the packetDiscount
        restPacketDiscountMockMvc
            .perform(get(ENTITY_API_URL_ID, packetDiscount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(packetDiscount.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.cost").value(sameNumber(DEFAULT_COST)));
    }

    @Test
    @Transactional
    void getNonExistingPacketDiscount() throws Exception {
        // Get the packetDiscount
        restPacketDiscountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPacketDiscount() throws Exception {
        // Initialize the database
        packetDiscountRepository.saveAndFlush(packetDiscount);

        int databaseSizeBeforeUpdate = packetDiscountRepository.findAll().size();

        // Update the packetDiscount
        PacketDiscount updatedPacketDiscount = packetDiscountRepository.findById(packetDiscount.getId()).get();
        // Disconnect from session so that the updates on updatedPacketDiscount are not directly saved in db
        em.detach(updatedPacketDiscount);
        updatedPacketDiscount.title(UPDATED_TITLE).cost(UPDATED_COST);

        restPacketDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPacketDiscount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPacketDiscount))
            )
            .andExpect(status().isOk());

        // Validate the PacketDiscount in the database
        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeUpdate);
        PacketDiscount testPacketDiscount = packetDiscountList.get(packetDiscountList.size() - 1);
        assertThat(testPacketDiscount.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPacketDiscount.getCost()).isEqualByComparingTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void putNonExistingPacketDiscount() throws Exception {
        int databaseSizeBeforeUpdate = packetDiscountRepository.findAll().size();
        packetDiscount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacketDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, packetDiscount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packetDiscount))
            )
            .andExpect(status().isBadRequest());

        // Validate the PacketDiscount in the database
        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPacketDiscount() throws Exception {
        int databaseSizeBeforeUpdate = packetDiscountRepository.findAll().size();
        packetDiscount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacketDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packetDiscount))
            )
            .andExpect(status().isBadRequest());

        // Validate the PacketDiscount in the database
        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPacketDiscount() throws Exception {
        int databaseSizeBeforeUpdate = packetDiscountRepository.findAll().size();
        packetDiscount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacketDiscountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packetDiscount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PacketDiscount in the database
        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePacketDiscountWithPatch() throws Exception {
        // Initialize the database
        packetDiscountRepository.saveAndFlush(packetDiscount);

        int databaseSizeBeforeUpdate = packetDiscountRepository.findAll().size();

        // Update the packetDiscount using partial update
        PacketDiscount partialUpdatedPacketDiscount = new PacketDiscount();
        partialUpdatedPacketDiscount.setId(packetDiscount.getId());

        partialUpdatedPacketDiscount.title(UPDATED_TITLE).cost(UPDATED_COST);

        restPacketDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPacketDiscount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPacketDiscount))
            )
            .andExpect(status().isOk());

        // Validate the PacketDiscount in the database
        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeUpdate);
        PacketDiscount testPacketDiscount = packetDiscountList.get(packetDiscountList.size() - 1);
        assertThat(testPacketDiscount.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPacketDiscount.getCost()).isEqualByComparingTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void fullUpdatePacketDiscountWithPatch() throws Exception {
        // Initialize the database
        packetDiscountRepository.saveAndFlush(packetDiscount);

        int databaseSizeBeforeUpdate = packetDiscountRepository.findAll().size();

        // Update the packetDiscount using partial update
        PacketDiscount partialUpdatedPacketDiscount = new PacketDiscount();
        partialUpdatedPacketDiscount.setId(packetDiscount.getId());

        partialUpdatedPacketDiscount.title(UPDATED_TITLE).cost(UPDATED_COST);

        restPacketDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPacketDiscount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPacketDiscount))
            )
            .andExpect(status().isOk());

        // Validate the PacketDiscount in the database
        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeUpdate);
        PacketDiscount testPacketDiscount = packetDiscountList.get(packetDiscountList.size() - 1);
        assertThat(testPacketDiscount.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPacketDiscount.getCost()).isEqualByComparingTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void patchNonExistingPacketDiscount() throws Exception {
        int databaseSizeBeforeUpdate = packetDiscountRepository.findAll().size();
        packetDiscount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacketDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, packetDiscount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packetDiscount))
            )
            .andExpect(status().isBadRequest());

        // Validate the PacketDiscount in the database
        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPacketDiscount() throws Exception {
        int databaseSizeBeforeUpdate = packetDiscountRepository.findAll().size();
        packetDiscount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacketDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packetDiscount))
            )
            .andExpect(status().isBadRequest());

        // Validate the PacketDiscount in the database
        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPacketDiscount() throws Exception {
        int databaseSizeBeforeUpdate = packetDiscountRepository.findAll().size();
        packetDiscount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacketDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(packetDiscount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PacketDiscount in the database
        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePacketDiscount() throws Exception {
        // Initialize the database
        packetDiscountRepository.saveAndFlush(packetDiscount);

        int databaseSizeBeforeDelete = packetDiscountRepository.findAll().size();

        // Delete the packetDiscount
        restPacketDiscountMockMvc
            .perform(delete(ENTITY_API_URL_ID, packetDiscount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PacketDiscount> packetDiscountList = packetDiscountRepository.findAll();
        assertThat(packetDiscountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
