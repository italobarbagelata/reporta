package cl.ibapps.reportapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.ibapps.reportapp.IntegrationTest;
import cl.ibapps.reportapp.domain.Specie;
import cl.ibapps.reportapp.repository.SpecieRepository;
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
 * Integration tests for the {@link SpecieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecieResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/species";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecieRepository specieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecieMockMvc;

    private Specie specie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specie createEntity(EntityManager em) {
        Specie specie = new Specie().description(DEFAULT_DESCRIPTION);
        return specie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specie createUpdatedEntity(EntityManager em) {
        Specie specie = new Specie().description(UPDATED_DESCRIPTION);
        return specie;
    }

    @BeforeEach
    public void initTest() {
        specie = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecie() throws Exception {
        int databaseSizeBeforeCreate = specieRepository.findAll().size();
        // Create the Specie
        restSpecieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specie)))
            .andExpect(status().isCreated());

        // Validate the Specie in the database
        List<Specie> specieList = specieRepository.findAll();
        assertThat(specieList).hasSize(databaseSizeBeforeCreate + 1);
        Specie testSpecie = specieList.get(specieList.size() - 1);
        assertThat(testSpecie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createSpecieWithExistingId() throws Exception {
        // Create the Specie with an existing ID
        specie.setId(1L);

        int databaseSizeBeforeCreate = specieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specie)))
            .andExpect(status().isBadRequest());

        // Validate the Specie in the database
        List<Specie> specieList = specieRepository.findAll();
        assertThat(specieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpecies() throws Exception {
        // Initialize the database
        specieRepository.saveAndFlush(specie);

        // Get all the specieList
        restSpecieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specie.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getSpecie() throws Exception {
        // Initialize the database
        specieRepository.saveAndFlush(specie);

        // Get the specie
        restSpecieMockMvc
            .perform(get(ENTITY_API_URL_ID, specie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specie.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingSpecie() throws Exception {
        // Get the specie
        restSpecieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSpecie() throws Exception {
        // Initialize the database
        specieRepository.saveAndFlush(specie);

        int databaseSizeBeforeUpdate = specieRepository.findAll().size();

        // Update the specie
        Specie updatedSpecie = specieRepository.findById(specie.getId()).get();
        // Disconnect from session so that the updates on updatedSpecie are not directly saved in db
        em.detach(updatedSpecie);
        updatedSpecie.description(UPDATED_DESCRIPTION);

        restSpecieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpecie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSpecie))
            )
            .andExpect(status().isOk());

        // Validate the Specie in the database
        List<Specie> specieList = specieRepository.findAll();
        assertThat(specieList).hasSize(databaseSizeBeforeUpdate);
        Specie testSpecie = specieList.get(specieList.size() - 1);
        assertThat(testSpecie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingSpecie() throws Exception {
        int databaseSizeBeforeUpdate = specieRepository.findAll().size();
        specie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specie in the database
        List<Specie> specieList = specieRepository.findAll();
        assertThat(specieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecie() throws Exception {
        int databaseSizeBeforeUpdate = specieRepository.findAll().size();
        specie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specie in the database
        List<Specie> specieList = specieRepository.findAll();
        assertThat(specieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecie() throws Exception {
        int databaseSizeBeforeUpdate = specieRepository.findAll().size();
        specie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specie in the database
        List<Specie> specieList = specieRepository.findAll();
        assertThat(specieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecieWithPatch() throws Exception {
        // Initialize the database
        specieRepository.saveAndFlush(specie);

        int databaseSizeBeforeUpdate = specieRepository.findAll().size();

        // Update the specie using partial update
        Specie partialUpdatedSpecie = new Specie();
        partialUpdatedSpecie.setId(specie.getId());

        restSpecieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecie))
            )
            .andExpect(status().isOk());

        // Validate the Specie in the database
        List<Specie> specieList = specieRepository.findAll();
        assertThat(specieList).hasSize(databaseSizeBeforeUpdate);
        Specie testSpecie = specieList.get(specieList.size() - 1);
        assertThat(testSpecie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateSpecieWithPatch() throws Exception {
        // Initialize the database
        specieRepository.saveAndFlush(specie);

        int databaseSizeBeforeUpdate = specieRepository.findAll().size();

        // Update the specie using partial update
        Specie partialUpdatedSpecie = new Specie();
        partialUpdatedSpecie.setId(specie.getId());

        partialUpdatedSpecie.description(UPDATED_DESCRIPTION);

        restSpecieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecie))
            )
            .andExpect(status().isOk());

        // Validate the Specie in the database
        List<Specie> specieList = specieRepository.findAll();
        assertThat(specieList).hasSize(databaseSizeBeforeUpdate);
        Specie testSpecie = specieList.get(specieList.size() - 1);
        assertThat(testSpecie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingSpecie() throws Exception {
        int databaseSizeBeforeUpdate = specieRepository.findAll().size();
        specie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specie in the database
        List<Specie> specieList = specieRepository.findAll();
        assertThat(specieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecie() throws Exception {
        int databaseSizeBeforeUpdate = specieRepository.findAll().size();
        specie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specie in the database
        List<Specie> specieList = specieRepository.findAll();
        assertThat(specieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecie() throws Exception {
        int databaseSizeBeforeUpdate = specieRepository.findAll().size();
        specie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecieMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(specie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specie in the database
        List<Specie> specieList = specieRepository.findAll();
        assertThat(specieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecie() throws Exception {
        // Initialize the database
        specieRepository.saveAndFlush(specie);

        int databaseSizeBeforeDelete = specieRepository.findAll().size();

        // Delete the specie
        restSpecieMockMvc
            .perform(delete(ENTITY_API_URL_ID, specie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Specie> specieList = specieRepository.findAll();
        assertThat(specieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
