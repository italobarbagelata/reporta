package cl.ibapps.reportapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.ibapps.reportapp.IntegrationTest;
import cl.ibapps.reportapp.domain.Variety;
import cl.ibapps.reportapp.repository.VarietyRepository;
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
 * Integration tests for the {@link VarietyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VarietyResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/varieties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VarietyRepository varietyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVarietyMockMvc;

    private Variety variety;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Variety createEntity(EntityManager em) {
        Variety variety = new Variety().description(DEFAULT_DESCRIPTION);
        return variety;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Variety createUpdatedEntity(EntityManager em) {
        Variety variety = new Variety().description(UPDATED_DESCRIPTION);
        return variety;
    }

    @BeforeEach
    public void initTest() {
        variety = createEntity(em);
    }

    @Test
    @Transactional
    void createVariety() throws Exception {
        int databaseSizeBeforeCreate = varietyRepository.findAll().size();
        // Create the Variety
        restVarietyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(variety)))
            .andExpect(status().isCreated());

        // Validate the Variety in the database
        List<Variety> varietyList = varietyRepository.findAll();
        assertThat(varietyList).hasSize(databaseSizeBeforeCreate + 1);
        Variety testVariety = varietyList.get(varietyList.size() - 1);
        assertThat(testVariety.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createVarietyWithExistingId() throws Exception {
        // Create the Variety with an existing ID
        variety.setId(1L);

        int databaseSizeBeforeCreate = varietyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVarietyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(variety)))
            .andExpect(status().isBadRequest());

        // Validate the Variety in the database
        List<Variety> varietyList = varietyRepository.findAll();
        assertThat(varietyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVarieties() throws Exception {
        // Initialize the database
        varietyRepository.saveAndFlush(variety);

        // Get all the varietyList
        restVarietyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(variety.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getVariety() throws Exception {
        // Initialize the database
        varietyRepository.saveAndFlush(variety);

        // Get the variety
        restVarietyMockMvc
            .perform(get(ENTITY_API_URL_ID, variety.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(variety.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingVariety() throws Exception {
        // Get the variety
        restVarietyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVariety() throws Exception {
        // Initialize the database
        varietyRepository.saveAndFlush(variety);

        int databaseSizeBeforeUpdate = varietyRepository.findAll().size();

        // Update the variety
        Variety updatedVariety = varietyRepository.findById(variety.getId()).get();
        // Disconnect from session so that the updates on updatedVariety are not directly saved in db
        em.detach(updatedVariety);
        updatedVariety.description(UPDATED_DESCRIPTION);

        restVarietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVariety.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVariety))
            )
            .andExpect(status().isOk());

        // Validate the Variety in the database
        List<Variety> varietyList = varietyRepository.findAll();
        assertThat(varietyList).hasSize(databaseSizeBeforeUpdate);
        Variety testVariety = varietyList.get(varietyList.size() - 1);
        assertThat(testVariety.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingVariety() throws Exception {
        int databaseSizeBeforeUpdate = varietyRepository.findAll().size();
        variety.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVarietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, variety.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(variety))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variety in the database
        List<Variety> varietyList = varietyRepository.findAll();
        assertThat(varietyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVariety() throws Exception {
        int databaseSizeBeforeUpdate = varietyRepository.findAll().size();
        variety.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(variety))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variety in the database
        List<Variety> varietyList = varietyRepository.findAll();
        assertThat(varietyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVariety() throws Exception {
        int databaseSizeBeforeUpdate = varietyRepository.findAll().size();
        variety.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarietyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(variety)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Variety in the database
        List<Variety> varietyList = varietyRepository.findAll();
        assertThat(varietyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVarietyWithPatch() throws Exception {
        // Initialize the database
        varietyRepository.saveAndFlush(variety);

        int databaseSizeBeforeUpdate = varietyRepository.findAll().size();

        // Update the variety using partial update
        Variety partialUpdatedVariety = new Variety();
        partialUpdatedVariety.setId(variety.getId());

        restVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVariety.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVariety))
            )
            .andExpect(status().isOk());

        // Validate the Variety in the database
        List<Variety> varietyList = varietyRepository.findAll();
        assertThat(varietyList).hasSize(databaseSizeBeforeUpdate);
        Variety testVariety = varietyList.get(varietyList.size() - 1);
        assertThat(testVariety.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateVarietyWithPatch() throws Exception {
        // Initialize the database
        varietyRepository.saveAndFlush(variety);

        int databaseSizeBeforeUpdate = varietyRepository.findAll().size();

        // Update the variety using partial update
        Variety partialUpdatedVariety = new Variety();
        partialUpdatedVariety.setId(variety.getId());

        partialUpdatedVariety.description(UPDATED_DESCRIPTION);

        restVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVariety.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVariety))
            )
            .andExpect(status().isOk());

        // Validate the Variety in the database
        List<Variety> varietyList = varietyRepository.findAll();
        assertThat(varietyList).hasSize(databaseSizeBeforeUpdate);
        Variety testVariety = varietyList.get(varietyList.size() - 1);
        assertThat(testVariety.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingVariety() throws Exception {
        int databaseSizeBeforeUpdate = varietyRepository.findAll().size();
        variety.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, variety.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(variety))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variety in the database
        List<Variety> varietyList = varietyRepository.findAll();
        assertThat(varietyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVariety() throws Exception {
        int databaseSizeBeforeUpdate = varietyRepository.findAll().size();
        variety.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(variety))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variety in the database
        List<Variety> varietyList = varietyRepository.findAll();
        assertThat(varietyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVariety() throws Exception {
        int databaseSizeBeforeUpdate = varietyRepository.findAll().size();
        variety.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarietyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(variety)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Variety in the database
        List<Variety> varietyList = varietyRepository.findAll();
        assertThat(varietyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVariety() throws Exception {
        // Initialize the database
        varietyRepository.saveAndFlush(variety);

        int databaseSizeBeforeDelete = varietyRepository.findAll().size();

        // Delete the variety
        restVarietyMockMvc
            .perform(delete(ENTITY_API_URL_ID, variety.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Variety> varietyList = varietyRepository.findAll();
        assertThat(varietyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
