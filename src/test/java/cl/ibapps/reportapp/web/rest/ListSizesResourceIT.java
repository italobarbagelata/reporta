package cl.ibapps.reportapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.ibapps.reportapp.IntegrationTest;
import cl.ibapps.reportapp.domain.ListSizes;
import cl.ibapps.reportapp.repository.ListSizesRepository;
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
 * Integration tests for the {@link ListSizesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ListSizesResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/list-sizes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ListSizesRepository listSizesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restListSizesMockMvc;

    private ListSizes listSizes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ListSizes createEntity(EntityManager em) {
        ListSizes listSizes = new ListSizes().description(DEFAULT_DESCRIPTION);
        return listSizes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ListSizes createUpdatedEntity(EntityManager em) {
        ListSizes listSizes = new ListSizes().description(UPDATED_DESCRIPTION);
        return listSizes;
    }

    @BeforeEach
    public void initTest() {
        listSizes = createEntity(em);
    }

    @Test
    @Transactional
    void createListSizes() throws Exception {
        int databaseSizeBeforeCreate = listSizesRepository.findAll().size();
        // Create the ListSizes
        restListSizesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(listSizes)))
            .andExpect(status().isCreated());

        // Validate the ListSizes in the database
        List<ListSizes> listSizesList = listSizesRepository.findAll();
        assertThat(listSizesList).hasSize(databaseSizeBeforeCreate + 1);
        ListSizes testListSizes = listSizesList.get(listSizesList.size() - 1);
        assertThat(testListSizes.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createListSizesWithExistingId() throws Exception {
        // Create the ListSizes with an existing ID
        listSizes.setId(1L);

        int databaseSizeBeforeCreate = listSizesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restListSizesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(listSizes)))
            .andExpect(status().isBadRequest());

        // Validate the ListSizes in the database
        List<ListSizes> listSizesList = listSizesRepository.findAll();
        assertThat(listSizesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllListSizes() throws Exception {
        // Initialize the database
        listSizesRepository.saveAndFlush(listSizes);

        // Get all the listSizesList
        restListSizesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(listSizes.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getListSizes() throws Exception {
        // Initialize the database
        listSizesRepository.saveAndFlush(listSizes);

        // Get the listSizes
        restListSizesMockMvc
            .perform(get(ENTITY_API_URL_ID, listSizes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(listSizes.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingListSizes() throws Exception {
        // Get the listSizes
        restListSizesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewListSizes() throws Exception {
        // Initialize the database
        listSizesRepository.saveAndFlush(listSizes);

        int databaseSizeBeforeUpdate = listSizesRepository.findAll().size();

        // Update the listSizes
        ListSizes updatedListSizes = listSizesRepository.findById(listSizes.getId()).get();
        // Disconnect from session so that the updates on updatedListSizes are not directly saved in db
        em.detach(updatedListSizes);
        updatedListSizes.description(UPDATED_DESCRIPTION);

        restListSizesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedListSizes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedListSizes))
            )
            .andExpect(status().isOk());

        // Validate the ListSizes in the database
        List<ListSizes> listSizesList = listSizesRepository.findAll();
        assertThat(listSizesList).hasSize(databaseSizeBeforeUpdate);
        ListSizes testListSizes = listSizesList.get(listSizesList.size() - 1);
        assertThat(testListSizes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingListSizes() throws Exception {
        int databaseSizeBeforeUpdate = listSizesRepository.findAll().size();
        listSizes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restListSizesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, listSizes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(listSizes))
            )
            .andExpect(status().isBadRequest());

        // Validate the ListSizes in the database
        List<ListSizes> listSizesList = listSizesRepository.findAll();
        assertThat(listSizesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchListSizes() throws Exception {
        int databaseSizeBeforeUpdate = listSizesRepository.findAll().size();
        listSizes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restListSizesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(listSizes))
            )
            .andExpect(status().isBadRequest());

        // Validate the ListSizes in the database
        List<ListSizes> listSizesList = listSizesRepository.findAll();
        assertThat(listSizesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamListSizes() throws Exception {
        int databaseSizeBeforeUpdate = listSizesRepository.findAll().size();
        listSizes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restListSizesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(listSizes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ListSizes in the database
        List<ListSizes> listSizesList = listSizesRepository.findAll();
        assertThat(listSizesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateListSizesWithPatch() throws Exception {
        // Initialize the database
        listSizesRepository.saveAndFlush(listSizes);

        int databaseSizeBeforeUpdate = listSizesRepository.findAll().size();

        // Update the listSizes using partial update
        ListSizes partialUpdatedListSizes = new ListSizes();
        partialUpdatedListSizes.setId(listSizes.getId());

        partialUpdatedListSizes.description(UPDATED_DESCRIPTION);

        restListSizesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedListSizes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedListSizes))
            )
            .andExpect(status().isOk());

        // Validate the ListSizes in the database
        List<ListSizes> listSizesList = listSizesRepository.findAll();
        assertThat(listSizesList).hasSize(databaseSizeBeforeUpdate);
        ListSizes testListSizes = listSizesList.get(listSizesList.size() - 1);
        assertThat(testListSizes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateListSizesWithPatch() throws Exception {
        // Initialize the database
        listSizesRepository.saveAndFlush(listSizes);

        int databaseSizeBeforeUpdate = listSizesRepository.findAll().size();

        // Update the listSizes using partial update
        ListSizes partialUpdatedListSizes = new ListSizes();
        partialUpdatedListSizes.setId(listSizes.getId());

        partialUpdatedListSizes.description(UPDATED_DESCRIPTION);

        restListSizesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedListSizes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedListSizes))
            )
            .andExpect(status().isOk());

        // Validate the ListSizes in the database
        List<ListSizes> listSizesList = listSizesRepository.findAll();
        assertThat(listSizesList).hasSize(databaseSizeBeforeUpdate);
        ListSizes testListSizes = listSizesList.get(listSizesList.size() - 1);
        assertThat(testListSizes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingListSizes() throws Exception {
        int databaseSizeBeforeUpdate = listSizesRepository.findAll().size();
        listSizes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restListSizesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, listSizes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(listSizes))
            )
            .andExpect(status().isBadRequest());

        // Validate the ListSizes in the database
        List<ListSizes> listSizesList = listSizesRepository.findAll();
        assertThat(listSizesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchListSizes() throws Exception {
        int databaseSizeBeforeUpdate = listSizesRepository.findAll().size();
        listSizes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restListSizesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(listSizes))
            )
            .andExpect(status().isBadRequest());

        // Validate the ListSizes in the database
        List<ListSizes> listSizesList = listSizesRepository.findAll();
        assertThat(listSizesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamListSizes() throws Exception {
        int databaseSizeBeforeUpdate = listSizesRepository.findAll().size();
        listSizes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restListSizesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(listSizes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ListSizes in the database
        List<ListSizes> listSizesList = listSizesRepository.findAll();
        assertThat(listSizesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteListSizes() throws Exception {
        // Initialize the database
        listSizesRepository.saveAndFlush(listSizes);

        int databaseSizeBeforeDelete = listSizesRepository.findAll().size();

        // Delete the listSizes
        restListSizesMockMvc
            .perform(delete(ENTITY_API_URL_ID, listSizes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ListSizes> listSizesList = listSizesRepository.findAll();
        assertThat(listSizesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
