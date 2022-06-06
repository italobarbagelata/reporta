package cl.ibapps.reportapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.ibapps.reportapp.IntegrationTest;
import cl.ibapps.reportapp.domain.Exporter;
import cl.ibapps.reportapp.repository.ExporterRepository;
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
 * Integration tests for the {@link ExporterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExporterResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/exporters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExporterRepository exporterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExporterMockMvc;

    private Exporter exporter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exporter createEntity(EntityManager em) {
        Exporter exporter = new Exporter().description(DEFAULT_DESCRIPTION);
        return exporter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exporter createUpdatedEntity(EntityManager em) {
        Exporter exporter = new Exporter().description(UPDATED_DESCRIPTION);
        return exporter;
    }

    @BeforeEach
    public void initTest() {
        exporter = createEntity(em);
    }

    @Test
    @Transactional
    void createExporter() throws Exception {
        int databaseSizeBeforeCreate = exporterRepository.findAll().size();
        // Create the Exporter
        restExporterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exporter)))
            .andExpect(status().isCreated());

        // Validate the Exporter in the database
        List<Exporter> exporterList = exporterRepository.findAll();
        assertThat(exporterList).hasSize(databaseSizeBeforeCreate + 1);
        Exporter testExporter = exporterList.get(exporterList.size() - 1);
        assertThat(testExporter.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createExporterWithExistingId() throws Exception {
        // Create the Exporter with an existing ID
        exporter.setId(1L);

        int databaseSizeBeforeCreate = exporterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExporterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exporter)))
            .andExpect(status().isBadRequest());

        // Validate the Exporter in the database
        List<Exporter> exporterList = exporterRepository.findAll();
        assertThat(exporterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExporters() throws Exception {
        // Initialize the database
        exporterRepository.saveAndFlush(exporter);

        // Get all the exporterList
        restExporterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exporter.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getExporter() throws Exception {
        // Initialize the database
        exporterRepository.saveAndFlush(exporter);

        // Get the exporter
        restExporterMockMvc
            .perform(get(ENTITY_API_URL_ID, exporter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exporter.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingExporter() throws Exception {
        // Get the exporter
        restExporterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExporter() throws Exception {
        // Initialize the database
        exporterRepository.saveAndFlush(exporter);

        int databaseSizeBeforeUpdate = exporterRepository.findAll().size();

        // Update the exporter
        Exporter updatedExporter = exporterRepository.findById(exporter.getId()).get();
        // Disconnect from session so that the updates on updatedExporter are not directly saved in db
        em.detach(updatedExporter);
        updatedExporter.description(UPDATED_DESCRIPTION);

        restExporterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExporter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExporter))
            )
            .andExpect(status().isOk());

        // Validate the Exporter in the database
        List<Exporter> exporterList = exporterRepository.findAll();
        assertThat(exporterList).hasSize(databaseSizeBeforeUpdate);
        Exporter testExporter = exporterList.get(exporterList.size() - 1);
        assertThat(testExporter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingExporter() throws Exception {
        int databaseSizeBeforeUpdate = exporterRepository.findAll().size();
        exporter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExporterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exporter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exporter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exporter in the database
        List<Exporter> exporterList = exporterRepository.findAll();
        assertThat(exporterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExporter() throws Exception {
        int databaseSizeBeforeUpdate = exporterRepository.findAll().size();
        exporter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExporterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exporter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exporter in the database
        List<Exporter> exporterList = exporterRepository.findAll();
        assertThat(exporterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExporter() throws Exception {
        int databaseSizeBeforeUpdate = exporterRepository.findAll().size();
        exporter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExporterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exporter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exporter in the database
        List<Exporter> exporterList = exporterRepository.findAll();
        assertThat(exporterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExporterWithPatch() throws Exception {
        // Initialize the database
        exporterRepository.saveAndFlush(exporter);

        int databaseSizeBeforeUpdate = exporterRepository.findAll().size();

        // Update the exporter using partial update
        Exporter partialUpdatedExporter = new Exporter();
        partialUpdatedExporter.setId(exporter.getId());

        partialUpdatedExporter.description(UPDATED_DESCRIPTION);

        restExporterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExporter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExporter))
            )
            .andExpect(status().isOk());

        // Validate the Exporter in the database
        List<Exporter> exporterList = exporterRepository.findAll();
        assertThat(exporterList).hasSize(databaseSizeBeforeUpdate);
        Exporter testExporter = exporterList.get(exporterList.size() - 1);
        assertThat(testExporter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateExporterWithPatch() throws Exception {
        // Initialize the database
        exporterRepository.saveAndFlush(exporter);

        int databaseSizeBeforeUpdate = exporterRepository.findAll().size();

        // Update the exporter using partial update
        Exporter partialUpdatedExporter = new Exporter();
        partialUpdatedExporter.setId(exporter.getId());

        partialUpdatedExporter.description(UPDATED_DESCRIPTION);

        restExporterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExporter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExporter))
            )
            .andExpect(status().isOk());

        // Validate the Exporter in the database
        List<Exporter> exporterList = exporterRepository.findAll();
        assertThat(exporterList).hasSize(databaseSizeBeforeUpdate);
        Exporter testExporter = exporterList.get(exporterList.size() - 1);
        assertThat(testExporter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingExporter() throws Exception {
        int databaseSizeBeforeUpdate = exporterRepository.findAll().size();
        exporter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExporterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exporter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exporter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exporter in the database
        List<Exporter> exporterList = exporterRepository.findAll();
        assertThat(exporterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExporter() throws Exception {
        int databaseSizeBeforeUpdate = exporterRepository.findAll().size();
        exporter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExporterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exporter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exporter in the database
        List<Exporter> exporterList = exporterRepository.findAll();
        assertThat(exporterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExporter() throws Exception {
        int databaseSizeBeforeUpdate = exporterRepository.findAll().size();
        exporter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExporterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(exporter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exporter in the database
        List<Exporter> exporterList = exporterRepository.findAll();
        assertThat(exporterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExporter() throws Exception {
        // Initialize the database
        exporterRepository.saveAndFlush(exporter);

        int databaseSizeBeforeDelete = exporterRepository.findAll().size();

        // Delete the exporter
        restExporterMockMvc
            .perform(delete(ENTITY_API_URL_ID, exporter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Exporter> exporterList = exporterRepository.findAll();
        assertThat(exporterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
