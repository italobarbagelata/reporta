package cl.ibapps.reportapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.ibapps.reportapp.IntegrationTest;
import cl.ibapps.reportapp.domain.Dispatchs;
import cl.ibapps.reportapp.repository.DispatchsRepository;
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
 * Integration tests for the {@link DispatchsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DispatchsResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dispatchs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DispatchsRepository dispatchsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDispatchsMockMvc;

    private Dispatchs dispatchs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispatchs createEntity(EntityManager em) {
        Dispatchs dispatchs = new Dispatchs().description(DEFAULT_DESCRIPTION);
        return dispatchs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispatchs createUpdatedEntity(EntityManager em) {
        Dispatchs dispatchs = new Dispatchs().description(UPDATED_DESCRIPTION);
        return dispatchs;
    }

    @BeforeEach
    public void initTest() {
        dispatchs = createEntity(em);
    }

    @Test
    @Transactional
    void createDispatchs() throws Exception {
        int databaseSizeBeforeCreate = dispatchsRepository.findAll().size();
        // Create the Dispatchs
        restDispatchsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispatchs)))
            .andExpect(status().isCreated());

        // Validate the Dispatchs in the database
        List<Dispatchs> dispatchsList = dispatchsRepository.findAll();
        assertThat(dispatchsList).hasSize(databaseSizeBeforeCreate + 1);
        Dispatchs testDispatchs = dispatchsList.get(dispatchsList.size() - 1);
        assertThat(testDispatchs.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createDispatchsWithExistingId() throws Exception {
        // Create the Dispatchs with an existing ID
        dispatchs.setId(1L);

        int databaseSizeBeforeCreate = dispatchsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDispatchsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispatchs)))
            .andExpect(status().isBadRequest());

        // Validate the Dispatchs in the database
        List<Dispatchs> dispatchsList = dispatchsRepository.findAll();
        assertThat(dispatchsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDispatchs() throws Exception {
        // Initialize the database
        dispatchsRepository.saveAndFlush(dispatchs);

        // Get all the dispatchsList
        restDispatchsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dispatchs.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getDispatchs() throws Exception {
        // Initialize the database
        dispatchsRepository.saveAndFlush(dispatchs);

        // Get the dispatchs
        restDispatchsMockMvc
            .perform(get(ENTITY_API_URL_ID, dispatchs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dispatchs.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingDispatchs() throws Exception {
        // Get the dispatchs
        restDispatchsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDispatchs() throws Exception {
        // Initialize the database
        dispatchsRepository.saveAndFlush(dispatchs);

        int databaseSizeBeforeUpdate = dispatchsRepository.findAll().size();

        // Update the dispatchs
        Dispatchs updatedDispatchs = dispatchsRepository.findById(dispatchs.getId()).get();
        // Disconnect from session so that the updates on updatedDispatchs are not directly saved in db
        em.detach(updatedDispatchs);
        updatedDispatchs.description(UPDATED_DESCRIPTION);

        restDispatchsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDispatchs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDispatchs))
            )
            .andExpect(status().isOk());

        // Validate the Dispatchs in the database
        List<Dispatchs> dispatchsList = dispatchsRepository.findAll();
        assertThat(dispatchsList).hasSize(databaseSizeBeforeUpdate);
        Dispatchs testDispatchs = dispatchsList.get(dispatchsList.size() - 1);
        assertThat(testDispatchs.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingDispatchs() throws Exception {
        int databaseSizeBeforeUpdate = dispatchsRepository.findAll().size();
        dispatchs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispatchsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispatchs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispatchs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispatchs in the database
        List<Dispatchs> dispatchsList = dispatchsRepository.findAll();
        assertThat(dispatchsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDispatchs() throws Exception {
        int databaseSizeBeforeUpdate = dispatchsRepository.findAll().size();
        dispatchs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispatchs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispatchs in the database
        List<Dispatchs> dispatchsList = dispatchsRepository.findAll();
        assertThat(dispatchsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDispatchs() throws Exception {
        int databaseSizeBeforeUpdate = dispatchsRepository.findAll().size();
        dispatchs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispatchs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispatchs in the database
        List<Dispatchs> dispatchsList = dispatchsRepository.findAll();
        assertThat(dispatchsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDispatchsWithPatch() throws Exception {
        // Initialize the database
        dispatchsRepository.saveAndFlush(dispatchs);

        int databaseSizeBeforeUpdate = dispatchsRepository.findAll().size();

        // Update the dispatchs using partial update
        Dispatchs partialUpdatedDispatchs = new Dispatchs();
        partialUpdatedDispatchs.setId(dispatchs.getId());

        partialUpdatedDispatchs.description(UPDATED_DESCRIPTION);

        restDispatchsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispatchs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDispatchs))
            )
            .andExpect(status().isOk());

        // Validate the Dispatchs in the database
        List<Dispatchs> dispatchsList = dispatchsRepository.findAll();
        assertThat(dispatchsList).hasSize(databaseSizeBeforeUpdate);
        Dispatchs testDispatchs = dispatchsList.get(dispatchsList.size() - 1);
        assertThat(testDispatchs.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateDispatchsWithPatch() throws Exception {
        // Initialize the database
        dispatchsRepository.saveAndFlush(dispatchs);

        int databaseSizeBeforeUpdate = dispatchsRepository.findAll().size();

        // Update the dispatchs using partial update
        Dispatchs partialUpdatedDispatchs = new Dispatchs();
        partialUpdatedDispatchs.setId(dispatchs.getId());

        partialUpdatedDispatchs.description(UPDATED_DESCRIPTION);

        restDispatchsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispatchs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDispatchs))
            )
            .andExpect(status().isOk());

        // Validate the Dispatchs in the database
        List<Dispatchs> dispatchsList = dispatchsRepository.findAll();
        assertThat(dispatchsList).hasSize(databaseSizeBeforeUpdate);
        Dispatchs testDispatchs = dispatchsList.get(dispatchsList.size() - 1);
        assertThat(testDispatchs.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingDispatchs() throws Exception {
        int databaseSizeBeforeUpdate = dispatchsRepository.findAll().size();
        dispatchs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispatchsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dispatchs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispatchs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispatchs in the database
        List<Dispatchs> dispatchsList = dispatchsRepository.findAll();
        assertThat(dispatchsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDispatchs() throws Exception {
        int databaseSizeBeforeUpdate = dispatchsRepository.findAll().size();
        dispatchs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispatchs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispatchs in the database
        List<Dispatchs> dispatchsList = dispatchsRepository.findAll();
        assertThat(dispatchsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDispatchs() throws Exception {
        int databaseSizeBeforeUpdate = dispatchsRepository.findAll().size();
        dispatchs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dispatchs))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispatchs in the database
        List<Dispatchs> dispatchsList = dispatchsRepository.findAll();
        assertThat(dispatchsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDispatchs() throws Exception {
        // Initialize the database
        dispatchsRepository.saveAndFlush(dispatchs);

        int databaseSizeBeforeDelete = dispatchsRepository.findAll().size();

        // Delete the dispatchs
        restDispatchsMockMvc
            .perform(delete(ENTITY_API_URL_ID, dispatchs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dispatchs> dispatchsList = dispatchsRepository.findAll();
        assertThat(dispatchsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
