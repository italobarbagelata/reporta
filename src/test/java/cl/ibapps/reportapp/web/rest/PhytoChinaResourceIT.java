package cl.ibapps.reportapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.ibapps.reportapp.IntegrationTest;
import cl.ibapps.reportapp.domain.PhytoChina;
import cl.ibapps.reportapp.repository.PhytoChinaRepository;
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
 * Integration tests for the {@link PhytoChinaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhytoChinaResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/phyto-chinas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PhytoChinaRepository phytoChinaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhytoChinaMockMvc;

    private PhytoChina phytoChina;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhytoChina createEntity(EntityManager em) {
        PhytoChina phytoChina = new PhytoChina().description(DEFAULT_DESCRIPTION);
        return phytoChina;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhytoChina createUpdatedEntity(EntityManager em) {
        PhytoChina phytoChina = new PhytoChina().description(UPDATED_DESCRIPTION);
        return phytoChina;
    }

    @BeforeEach
    public void initTest() {
        phytoChina = createEntity(em);
    }

    @Test
    @Transactional
    void createPhytoChina() throws Exception {
        int databaseSizeBeforeCreate = phytoChinaRepository.findAll().size();
        // Create the PhytoChina
        restPhytoChinaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phytoChina)))
            .andExpect(status().isCreated());

        // Validate the PhytoChina in the database
        List<PhytoChina> phytoChinaList = phytoChinaRepository.findAll();
        assertThat(phytoChinaList).hasSize(databaseSizeBeforeCreate + 1);
        PhytoChina testPhytoChina = phytoChinaList.get(phytoChinaList.size() - 1);
        assertThat(testPhytoChina.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createPhytoChinaWithExistingId() throws Exception {
        // Create the PhytoChina with an existing ID
        phytoChina.setId(1L);

        int databaseSizeBeforeCreate = phytoChinaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhytoChinaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phytoChina)))
            .andExpect(status().isBadRequest());

        // Validate the PhytoChina in the database
        List<PhytoChina> phytoChinaList = phytoChinaRepository.findAll();
        assertThat(phytoChinaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPhytoChinas() throws Exception {
        // Initialize the database
        phytoChinaRepository.saveAndFlush(phytoChina);

        // Get all the phytoChinaList
        restPhytoChinaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phytoChina.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPhytoChina() throws Exception {
        // Initialize the database
        phytoChinaRepository.saveAndFlush(phytoChina);

        // Get the phytoChina
        restPhytoChinaMockMvc
            .perform(get(ENTITY_API_URL_ID, phytoChina.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phytoChina.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPhytoChina() throws Exception {
        // Get the phytoChina
        restPhytoChinaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPhytoChina() throws Exception {
        // Initialize the database
        phytoChinaRepository.saveAndFlush(phytoChina);

        int databaseSizeBeforeUpdate = phytoChinaRepository.findAll().size();

        // Update the phytoChina
        PhytoChina updatedPhytoChina = phytoChinaRepository.findById(phytoChina.getId()).get();
        // Disconnect from session so that the updates on updatedPhytoChina are not directly saved in db
        em.detach(updatedPhytoChina);
        updatedPhytoChina.description(UPDATED_DESCRIPTION);

        restPhytoChinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPhytoChina.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPhytoChina))
            )
            .andExpect(status().isOk());

        // Validate the PhytoChina in the database
        List<PhytoChina> phytoChinaList = phytoChinaRepository.findAll();
        assertThat(phytoChinaList).hasSize(databaseSizeBeforeUpdate);
        PhytoChina testPhytoChina = phytoChinaList.get(phytoChinaList.size() - 1);
        assertThat(testPhytoChina.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingPhytoChina() throws Exception {
        int databaseSizeBeforeUpdate = phytoChinaRepository.findAll().size();
        phytoChina.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhytoChinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phytoChina.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phytoChina))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhytoChina in the database
        List<PhytoChina> phytoChinaList = phytoChinaRepository.findAll();
        assertThat(phytoChinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhytoChina() throws Exception {
        int databaseSizeBeforeUpdate = phytoChinaRepository.findAll().size();
        phytoChina.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhytoChinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phytoChina))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhytoChina in the database
        List<PhytoChina> phytoChinaList = phytoChinaRepository.findAll();
        assertThat(phytoChinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhytoChina() throws Exception {
        int databaseSizeBeforeUpdate = phytoChinaRepository.findAll().size();
        phytoChina.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhytoChinaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phytoChina)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhytoChina in the database
        List<PhytoChina> phytoChinaList = phytoChinaRepository.findAll();
        assertThat(phytoChinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhytoChinaWithPatch() throws Exception {
        // Initialize the database
        phytoChinaRepository.saveAndFlush(phytoChina);

        int databaseSizeBeforeUpdate = phytoChinaRepository.findAll().size();

        // Update the phytoChina using partial update
        PhytoChina partialUpdatedPhytoChina = new PhytoChina();
        partialUpdatedPhytoChina.setId(phytoChina.getId());

        partialUpdatedPhytoChina.description(UPDATED_DESCRIPTION);

        restPhytoChinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhytoChina.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhytoChina))
            )
            .andExpect(status().isOk());

        // Validate the PhytoChina in the database
        List<PhytoChina> phytoChinaList = phytoChinaRepository.findAll();
        assertThat(phytoChinaList).hasSize(databaseSizeBeforeUpdate);
        PhytoChina testPhytoChina = phytoChinaList.get(phytoChinaList.size() - 1);
        assertThat(testPhytoChina.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePhytoChinaWithPatch() throws Exception {
        // Initialize the database
        phytoChinaRepository.saveAndFlush(phytoChina);

        int databaseSizeBeforeUpdate = phytoChinaRepository.findAll().size();

        // Update the phytoChina using partial update
        PhytoChina partialUpdatedPhytoChina = new PhytoChina();
        partialUpdatedPhytoChina.setId(phytoChina.getId());

        partialUpdatedPhytoChina.description(UPDATED_DESCRIPTION);

        restPhytoChinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhytoChina.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhytoChina))
            )
            .andExpect(status().isOk());

        // Validate the PhytoChina in the database
        List<PhytoChina> phytoChinaList = phytoChinaRepository.findAll();
        assertThat(phytoChinaList).hasSize(databaseSizeBeforeUpdate);
        PhytoChina testPhytoChina = phytoChinaList.get(phytoChinaList.size() - 1);
        assertThat(testPhytoChina.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPhytoChina() throws Exception {
        int databaseSizeBeforeUpdate = phytoChinaRepository.findAll().size();
        phytoChina.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhytoChinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, phytoChina.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phytoChina))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhytoChina in the database
        List<PhytoChina> phytoChinaList = phytoChinaRepository.findAll();
        assertThat(phytoChinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhytoChina() throws Exception {
        int databaseSizeBeforeUpdate = phytoChinaRepository.findAll().size();
        phytoChina.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhytoChinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phytoChina))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhytoChina in the database
        List<PhytoChina> phytoChinaList = phytoChinaRepository.findAll();
        assertThat(phytoChinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhytoChina() throws Exception {
        int databaseSizeBeforeUpdate = phytoChinaRepository.findAll().size();
        phytoChina.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhytoChinaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(phytoChina))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhytoChina in the database
        List<PhytoChina> phytoChinaList = phytoChinaRepository.findAll();
        assertThat(phytoChinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhytoChina() throws Exception {
        // Initialize the database
        phytoChinaRepository.saveAndFlush(phytoChina);

        int databaseSizeBeforeDelete = phytoChinaRepository.findAll().size();

        // Delete the phytoChina
        restPhytoChinaMockMvc
            .perform(delete(ENTITY_API_URL_ID, phytoChina.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PhytoChina> phytoChinaList = phytoChinaRepository.findAll();
        assertThat(phytoChinaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
