package cl.ibapps.reportapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.ibapps.reportapp.IntegrationTest;
import cl.ibapps.reportapp.domain.Packages;
import cl.ibapps.reportapp.repository.PackagesRepository;
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
 * Integration tests for the {@link PackagesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PackagesResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/packages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PackagesRepository packagesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPackagesMockMvc;

    private Packages packages;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Packages createEntity(EntityManager em) {
        Packages packages = new Packages().description(DEFAULT_DESCRIPTION);
        return packages;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Packages createUpdatedEntity(EntityManager em) {
        Packages packages = new Packages().description(UPDATED_DESCRIPTION);
        return packages;
    }

    @BeforeEach
    public void initTest() {
        packages = createEntity(em);
    }

    @Test
    @Transactional
    void createPackages() throws Exception {
        int databaseSizeBeforeCreate = packagesRepository.findAll().size();
        // Create the Packages
        restPackagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packages)))
            .andExpect(status().isCreated());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeCreate + 1);
        Packages testPackages = packagesList.get(packagesList.size() - 1);
        assertThat(testPackages.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createPackagesWithExistingId() throws Exception {
        // Create the Packages with an existing ID
        packages.setId(1L);

        int databaseSizeBeforeCreate = packagesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packages)))
            .andExpect(status().isBadRequest());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPackages() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList
        restPackagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packages.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPackages() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get the packages
        restPackagesMockMvc
            .perform(get(ENTITY_API_URL_ID, packages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(packages.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPackages() throws Exception {
        // Get the packages
        restPackagesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPackages() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        int databaseSizeBeforeUpdate = packagesRepository.findAll().size();

        // Update the packages
        Packages updatedPackages = packagesRepository.findById(packages.getId()).get();
        // Disconnect from session so that the updates on updatedPackages are not directly saved in db
        em.detach(updatedPackages);
        updatedPackages.description(UPDATED_DESCRIPTION);

        restPackagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPackages.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPackages))
            )
            .andExpect(status().isOk());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeUpdate);
        Packages testPackages = packagesList.get(packagesList.size() - 1);
        assertThat(testPackages.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingPackages() throws Exception {
        int databaseSizeBeforeUpdate = packagesRepository.findAll().size();
        packages.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, packages.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packages))
            )
            .andExpect(status().isBadRequest());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPackages() throws Exception {
        int databaseSizeBeforeUpdate = packagesRepository.findAll().size();
        packages.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packages))
            )
            .andExpect(status().isBadRequest());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPackages() throws Exception {
        int databaseSizeBeforeUpdate = packagesRepository.findAll().size();
        packages.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackagesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packages)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePackagesWithPatch() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        int databaseSizeBeforeUpdate = packagesRepository.findAll().size();

        // Update the packages using partial update
        Packages partialUpdatedPackages = new Packages();
        partialUpdatedPackages.setId(packages.getId());

        restPackagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPackages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPackages))
            )
            .andExpect(status().isOk());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeUpdate);
        Packages testPackages = packagesList.get(packagesList.size() - 1);
        assertThat(testPackages.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePackagesWithPatch() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        int databaseSizeBeforeUpdate = packagesRepository.findAll().size();

        // Update the packages using partial update
        Packages partialUpdatedPackages = new Packages();
        partialUpdatedPackages.setId(packages.getId());

        partialUpdatedPackages.description(UPDATED_DESCRIPTION);

        restPackagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPackages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPackages))
            )
            .andExpect(status().isOk());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeUpdate);
        Packages testPackages = packagesList.get(packagesList.size() - 1);
        assertThat(testPackages.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPackages() throws Exception {
        int databaseSizeBeforeUpdate = packagesRepository.findAll().size();
        packages.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, packages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packages))
            )
            .andExpect(status().isBadRequest());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPackages() throws Exception {
        int databaseSizeBeforeUpdate = packagesRepository.findAll().size();
        packages.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packages))
            )
            .andExpect(status().isBadRequest());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPackages() throws Exception {
        int databaseSizeBeforeUpdate = packagesRepository.findAll().size();
        packages.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackagesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(packages)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePackages() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        int databaseSizeBeforeDelete = packagesRepository.findAll().size();

        // Delete the packages
        restPackagesMockMvc
            .perform(delete(ENTITY_API_URL_ID, packages.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
