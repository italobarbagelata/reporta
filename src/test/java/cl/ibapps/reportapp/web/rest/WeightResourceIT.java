package cl.ibapps.reportapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.ibapps.reportapp.IntegrationTest;
import cl.ibapps.reportapp.domain.Weight;
import cl.ibapps.reportapp.repository.WeightRepository;
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

/**
 * Integration tests for the {@link WeightResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WeightResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/weights";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WeightRepository weightRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeightMockMvc;

    private Weight weight;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weight createEntity(EntityManager em) {
        Weight weight = new Weight().description(DEFAULT_DESCRIPTION);
        return weight;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weight createUpdatedEntity(EntityManager em) {
        Weight weight = new Weight().description(UPDATED_DESCRIPTION);
        return weight;
    }

    @BeforeEach
    public void initTest() {
        weight = createEntity(em);
    }

    @Test
    @Transactional
    void createWeight() throws Exception {
        int databaseSizeBeforeCreate = weightRepository.findAll().size();
        // Create the Weight
        restWeightMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(weight)))
            .andExpect(status().isCreated());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeCreate + 1);
        Weight testWeight = weightList.get(weightList.size() - 1);
        assertThat(testWeight.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createWeightWithExistingId() throws Exception {
        // Create the Weight with an existing ID
        weight.setId(1L);

        int databaseSizeBeforeCreate = weightRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeightMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(weight)))
            .andExpect(status().isBadRequest());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWeights() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        // Get all the weightList
        restWeightMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weight.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getWeight() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        // Get the weight
        restWeightMockMvc
            .perform(get(ENTITY_API_URL_ID, weight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weight.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingWeight() throws Exception {
        // Get the weight
        restWeightMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWeight() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        int databaseSizeBeforeUpdate = weightRepository.findAll().size();

        // Update the weight
        Weight updatedWeight = weightRepository.findById(weight.getId()).get();
        // Disconnect from session so that the updates on updatedWeight are not directly saved in db
        em.detach(updatedWeight);
        updatedWeight.description(UPDATED_DESCRIPTION);

        restWeightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWeight.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWeight))
            )
            .andExpect(status().isOk());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeUpdate);
        Weight testWeight = weightList.get(weightList.size() - 1);
        assertThat(testWeight.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingWeight() throws Exception {
        int databaseSizeBeforeUpdate = weightRepository.findAll().size();
        weight.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weight.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weight))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWeight() throws Exception {
        int databaseSizeBeforeUpdate = weightRepository.findAll().size();
        weight.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weight))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWeight() throws Exception {
        int databaseSizeBeforeUpdate = weightRepository.findAll().size();
        weight.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeightMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(weight)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWeightWithPatch() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        int databaseSizeBeforeUpdate = weightRepository.findAll().size();

        // Update the weight using partial update
        Weight partialUpdatedWeight = new Weight();
        partialUpdatedWeight.setId(weight.getId());

        restWeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeight.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeight))
            )
            .andExpect(status().isOk());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeUpdate);
        Weight testWeight = weightList.get(weightList.size() - 1);
        assertThat(testWeight.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateWeightWithPatch() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        int databaseSizeBeforeUpdate = weightRepository.findAll().size();

        // Update the weight using partial update
        Weight partialUpdatedWeight = new Weight();
        partialUpdatedWeight.setId(weight.getId());

        partialUpdatedWeight.description(UPDATED_DESCRIPTION);

        restWeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeight.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeight))
            )
            .andExpect(status().isOk());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeUpdate);
        Weight testWeight = weightList.get(weightList.size() - 1);
        assertThat(testWeight.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingWeight() throws Exception {
        int databaseSizeBeforeUpdate = weightRepository.findAll().size();
        weight.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, weight.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weight))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWeight() throws Exception {
        int databaseSizeBeforeUpdate = weightRepository.findAll().size();
        weight.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weight))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWeight() throws Exception {
        int databaseSizeBeforeUpdate = weightRepository.findAll().size();
        weight.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeightMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(weight)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWeight() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        int databaseSizeBeforeDelete = weightRepository.findAll().size();

        // Delete the weight
        restWeightMockMvc
            .perform(delete(ENTITY_API_URL_ID, weight.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
