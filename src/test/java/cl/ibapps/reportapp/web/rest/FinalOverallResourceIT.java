package cl.ibapps.reportapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.ibapps.reportapp.IntegrationTest;
import cl.ibapps.reportapp.domain.FinalOverall;
import cl.ibapps.reportapp.repository.FinalOverallRepository;
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
 * Integration tests for the {@link FinalOverallResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FinalOverallResourceIT {

    private static final Integer DEFAULT_NOTE = 1;
    private static final Integer UPDATED_NOTE = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/final-overalls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FinalOverallRepository finalOverallRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFinalOverallMockMvc;

    private FinalOverall finalOverall;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinalOverall createEntity(EntityManager em) {
        FinalOverall finalOverall = new FinalOverall().note(DEFAULT_NOTE).description(DEFAULT_DESCRIPTION);
        return finalOverall;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinalOverall createUpdatedEntity(EntityManager em) {
        FinalOverall finalOverall = new FinalOverall().note(UPDATED_NOTE).description(UPDATED_DESCRIPTION);
        return finalOverall;
    }

    @BeforeEach
    public void initTest() {
        finalOverall = createEntity(em);
    }

    @Test
    @Transactional
    void createFinalOverall() throws Exception {
        int databaseSizeBeforeCreate = finalOverallRepository.findAll().size();
        // Create the FinalOverall
        restFinalOverallMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(finalOverall)))
            .andExpect(status().isCreated());

        // Validate the FinalOverall in the database
        List<FinalOverall> finalOverallList = finalOverallRepository.findAll();
        assertThat(finalOverallList).hasSize(databaseSizeBeforeCreate + 1);
        FinalOverall testFinalOverall = finalOverallList.get(finalOverallList.size() - 1);
        assertThat(testFinalOverall.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testFinalOverall.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createFinalOverallWithExistingId() throws Exception {
        // Create the FinalOverall with an existing ID
        finalOverall.setId(1L);

        int databaseSizeBeforeCreate = finalOverallRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinalOverallMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(finalOverall)))
            .andExpect(status().isBadRequest());

        // Validate the FinalOverall in the database
        List<FinalOverall> finalOverallList = finalOverallRepository.findAll();
        assertThat(finalOverallList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFinalOveralls() throws Exception {
        // Initialize the database
        finalOverallRepository.saveAndFlush(finalOverall);

        // Get all the finalOverallList
        restFinalOverallMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finalOverall.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFinalOverall() throws Exception {
        // Initialize the database
        finalOverallRepository.saveAndFlush(finalOverall);

        // Get the finalOverall
        restFinalOverallMockMvc
            .perform(get(ENTITY_API_URL_ID, finalOverall.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(finalOverall.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingFinalOverall() throws Exception {
        // Get the finalOverall
        restFinalOverallMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFinalOverall() throws Exception {
        // Initialize the database
        finalOverallRepository.saveAndFlush(finalOverall);

        int databaseSizeBeforeUpdate = finalOverallRepository.findAll().size();

        // Update the finalOverall
        FinalOverall updatedFinalOverall = finalOverallRepository.findById(finalOverall.getId()).get();
        // Disconnect from session so that the updates on updatedFinalOverall are not directly saved in db
        em.detach(updatedFinalOverall);
        updatedFinalOverall.note(UPDATED_NOTE).description(UPDATED_DESCRIPTION);

        restFinalOverallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFinalOverall.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFinalOverall))
            )
            .andExpect(status().isOk());

        // Validate the FinalOverall in the database
        List<FinalOverall> finalOverallList = finalOverallRepository.findAll();
        assertThat(finalOverallList).hasSize(databaseSizeBeforeUpdate);
        FinalOverall testFinalOverall = finalOverallList.get(finalOverallList.size() - 1);
        assertThat(testFinalOverall.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testFinalOverall.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingFinalOverall() throws Exception {
        int databaseSizeBeforeUpdate = finalOverallRepository.findAll().size();
        finalOverall.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinalOverallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, finalOverall.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(finalOverall))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalOverall in the database
        List<FinalOverall> finalOverallList = finalOverallRepository.findAll();
        assertThat(finalOverallList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFinalOverall() throws Exception {
        int databaseSizeBeforeUpdate = finalOverallRepository.findAll().size();
        finalOverall.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinalOverallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(finalOverall))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalOverall in the database
        List<FinalOverall> finalOverallList = finalOverallRepository.findAll();
        assertThat(finalOverallList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFinalOverall() throws Exception {
        int databaseSizeBeforeUpdate = finalOverallRepository.findAll().size();
        finalOverall.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinalOverallMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(finalOverall)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinalOverall in the database
        List<FinalOverall> finalOverallList = finalOverallRepository.findAll();
        assertThat(finalOverallList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFinalOverallWithPatch() throws Exception {
        // Initialize the database
        finalOverallRepository.saveAndFlush(finalOverall);

        int databaseSizeBeforeUpdate = finalOverallRepository.findAll().size();

        // Update the finalOverall using partial update
        FinalOverall partialUpdatedFinalOverall = new FinalOverall();
        partialUpdatedFinalOverall.setId(finalOverall.getId());

        partialUpdatedFinalOverall.description(UPDATED_DESCRIPTION);

        restFinalOverallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinalOverall.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFinalOverall))
            )
            .andExpect(status().isOk());

        // Validate the FinalOverall in the database
        List<FinalOverall> finalOverallList = finalOverallRepository.findAll();
        assertThat(finalOverallList).hasSize(databaseSizeBeforeUpdate);
        FinalOverall testFinalOverall = finalOverallList.get(finalOverallList.size() - 1);
        assertThat(testFinalOverall.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testFinalOverall.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateFinalOverallWithPatch() throws Exception {
        // Initialize the database
        finalOverallRepository.saveAndFlush(finalOverall);

        int databaseSizeBeforeUpdate = finalOverallRepository.findAll().size();

        // Update the finalOverall using partial update
        FinalOverall partialUpdatedFinalOverall = new FinalOverall();
        partialUpdatedFinalOverall.setId(finalOverall.getId());

        partialUpdatedFinalOverall.note(UPDATED_NOTE).description(UPDATED_DESCRIPTION);

        restFinalOverallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinalOverall.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFinalOverall))
            )
            .andExpect(status().isOk());

        // Validate the FinalOverall in the database
        List<FinalOverall> finalOverallList = finalOverallRepository.findAll();
        assertThat(finalOverallList).hasSize(databaseSizeBeforeUpdate);
        FinalOverall testFinalOverall = finalOverallList.get(finalOverallList.size() - 1);
        assertThat(testFinalOverall.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testFinalOverall.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingFinalOverall() throws Exception {
        int databaseSizeBeforeUpdate = finalOverallRepository.findAll().size();
        finalOverall.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinalOverallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, finalOverall.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(finalOverall))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalOverall in the database
        List<FinalOverall> finalOverallList = finalOverallRepository.findAll();
        assertThat(finalOverallList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFinalOverall() throws Exception {
        int databaseSizeBeforeUpdate = finalOverallRepository.findAll().size();
        finalOverall.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinalOverallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(finalOverall))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalOverall in the database
        List<FinalOverall> finalOverallList = finalOverallRepository.findAll();
        assertThat(finalOverallList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFinalOverall() throws Exception {
        int databaseSizeBeforeUpdate = finalOverallRepository.findAll().size();
        finalOverall.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinalOverallMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(finalOverall))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinalOverall in the database
        List<FinalOverall> finalOverallList = finalOverallRepository.findAll();
        assertThat(finalOverallList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFinalOverall() throws Exception {
        // Initialize the database
        finalOverallRepository.saveAndFlush(finalOverall);

        int databaseSizeBeforeDelete = finalOverallRepository.findAll().size();

        // Delete the finalOverall
        restFinalOverallMockMvc
            .perform(delete(ENTITY_API_URL_ID, finalOverall.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FinalOverall> finalOverallList = finalOverallRepository.findAll();
        assertThat(finalOverallList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
