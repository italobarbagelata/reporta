package cl.ibapps.reportapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.ibapps.reportapp.IntegrationTest;
import cl.ibapps.reportapp.domain.Inspection;
import cl.ibapps.reportapp.repository.InspectionRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InspectionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InspectionResourceIT {

    private static final Instant DEFAULT_REPORT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REPORT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE = "BBBBBBBBBB";

    private static final Instant DEFAULT_INSPECTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSPECTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_GROWER = "AAAAAAAAAA";
    private static final String UPDATED_GROWER = "BBBBBBBBBB";

    private static final Instant DEFAULT_PACKING_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PACKING_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS = "BBBBBBBBBB";

    private static final String DEFAULT_FINAL_RECOMENDATIONS = "AAAAAAAAAA";
    private static final String UPDATED_FINAL_RECOMENDATIONS = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRA_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inspections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InspectionRepository inspectionRepository;

    @Mock
    private InspectionRepository inspectionRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInspectionMockMvc;

    private Inspection inspection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspection createEntity(EntityManager em) {
        Inspection inspection = new Inspection()
            .reportDate(DEFAULT_REPORT_DATE)
            .description(DEFAULT_DESCRIPTION)
            .place(DEFAULT_PLACE)
            .inspectionDate(DEFAULT_INSPECTION_DATE)
            .grower(DEFAULT_GROWER)
            .packingDate(DEFAULT_PACKING_DATE)
            .observations(DEFAULT_OBSERVATIONS)
            .finalRecomendations(DEFAULT_FINAL_RECOMENDATIONS)
            .extraDetails(DEFAULT_EXTRA_DETAILS);
        return inspection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspection createUpdatedEntity(EntityManager em) {
        Inspection inspection = new Inspection()
            .reportDate(UPDATED_REPORT_DATE)
            .description(UPDATED_DESCRIPTION)
            .place(UPDATED_PLACE)
            .inspectionDate(UPDATED_INSPECTION_DATE)
            .grower(UPDATED_GROWER)
            .packingDate(UPDATED_PACKING_DATE)
            .observations(UPDATED_OBSERVATIONS)
            .finalRecomendations(UPDATED_FINAL_RECOMENDATIONS)
            .extraDetails(UPDATED_EXTRA_DETAILS);
        return inspection;
    }

    @BeforeEach
    public void initTest() {
        inspection = createEntity(em);
    }

    @Test
    @Transactional
    void createInspection() throws Exception {
        int databaseSizeBeforeCreate = inspectionRepository.findAll().size();
        // Create the Inspection
        restInspectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspection)))
            .andExpect(status().isCreated());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeCreate + 1);
        Inspection testInspection = inspectionList.get(inspectionList.size() - 1);
        assertThat(testInspection.getReportDate()).isEqualTo(DEFAULT_REPORT_DATE);
        assertThat(testInspection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInspection.getPlace()).isEqualTo(DEFAULT_PLACE);
        assertThat(testInspection.getInspectionDate()).isEqualTo(DEFAULT_INSPECTION_DATE);
        assertThat(testInspection.getGrower()).isEqualTo(DEFAULT_GROWER);
        assertThat(testInspection.getPackingDate()).isEqualTo(DEFAULT_PACKING_DATE);
        assertThat(testInspection.getObservations()).isEqualTo(DEFAULT_OBSERVATIONS);
        assertThat(testInspection.getFinalRecomendations()).isEqualTo(DEFAULT_FINAL_RECOMENDATIONS);
        assertThat(testInspection.getExtraDetails()).isEqualTo(DEFAULT_EXTRA_DETAILS);
    }

    @Test
    @Transactional
    void createInspectionWithExistingId() throws Exception {
        // Create the Inspection with an existing ID
        inspection.setId(1L);

        int databaseSizeBeforeCreate = inspectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInspectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspection)))
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInspections() throws Exception {
        // Initialize the database
        inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList
        restInspectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inspection.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
            .andExpect(jsonPath("$.[*].inspectionDate").value(hasItem(DEFAULT_INSPECTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].grower").value(hasItem(DEFAULT_GROWER)))
            .andExpect(jsonPath("$.[*].packingDate").value(hasItem(DEFAULT_PACKING_DATE.toString())))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS)))
            .andExpect(jsonPath("$.[*].finalRecomendations").value(hasItem(DEFAULT_FINAL_RECOMENDATIONS)))
            .andExpect(jsonPath("$.[*].extraDetails").value(hasItem(DEFAULT_EXTRA_DETAILS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInspectionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(inspectionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInspectionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inspectionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInspectionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inspectionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInspectionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inspectionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getInspection() throws Exception {
        // Initialize the database
        inspectionRepository.saveAndFlush(inspection);

        // Get the inspection
        restInspectionMockMvc
            .perform(get(ENTITY_API_URL_ID, inspection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inspection.getId().intValue()))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE))
            .andExpect(jsonPath("$.inspectionDate").value(DEFAULT_INSPECTION_DATE.toString()))
            .andExpect(jsonPath("$.grower").value(DEFAULT_GROWER))
            .andExpect(jsonPath("$.packingDate").value(DEFAULT_PACKING_DATE.toString()))
            .andExpect(jsonPath("$.observations").value(DEFAULT_OBSERVATIONS))
            .andExpect(jsonPath("$.finalRecomendations").value(DEFAULT_FINAL_RECOMENDATIONS))
            .andExpect(jsonPath("$.extraDetails").value(DEFAULT_EXTRA_DETAILS));
    }

    @Test
    @Transactional
    void getNonExistingInspection() throws Exception {
        // Get the inspection
        restInspectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInspection() throws Exception {
        // Initialize the database
        inspectionRepository.saveAndFlush(inspection);

        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();

        // Update the inspection
        Inspection updatedInspection = inspectionRepository.findById(inspection.getId()).get();
        // Disconnect from session so that the updates on updatedInspection are not directly saved in db
        em.detach(updatedInspection);
        updatedInspection
            .reportDate(UPDATED_REPORT_DATE)
            .description(UPDATED_DESCRIPTION)
            .place(UPDATED_PLACE)
            .inspectionDate(UPDATED_INSPECTION_DATE)
            .grower(UPDATED_GROWER)
            .packingDate(UPDATED_PACKING_DATE)
            .observations(UPDATED_OBSERVATIONS)
            .finalRecomendations(UPDATED_FINAL_RECOMENDATIONS)
            .extraDetails(UPDATED_EXTRA_DETAILS);

        restInspectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInspection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInspection))
            )
            .andExpect(status().isOk());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
        Inspection testInspection = inspectionList.get(inspectionList.size() - 1);
        assertThat(testInspection.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testInspection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInspection.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testInspection.getInspectionDate()).isEqualTo(UPDATED_INSPECTION_DATE);
        assertThat(testInspection.getGrower()).isEqualTo(UPDATED_GROWER);
        assertThat(testInspection.getPackingDate()).isEqualTo(UPDATED_PACKING_DATE);
        assertThat(testInspection.getObservations()).isEqualTo(UPDATED_OBSERVATIONS);
        assertThat(testInspection.getFinalRecomendations()).isEqualTo(UPDATED_FINAL_RECOMENDATIONS);
        assertThat(testInspection.getExtraDetails()).isEqualTo(UPDATED_EXTRA_DETAILS);
    }

    @Test
    @Transactional
    void putNonExistingInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();
        inspection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();
        inspection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();
        inspection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInspectionWithPatch() throws Exception {
        // Initialize the database
        inspectionRepository.saveAndFlush(inspection);

        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();

        // Update the inspection using partial update
        Inspection partialUpdatedInspection = new Inspection();
        partialUpdatedInspection.setId(inspection.getId());

        partialUpdatedInspection
            .reportDate(UPDATED_REPORT_DATE)
            .place(UPDATED_PLACE)
            .inspectionDate(UPDATED_INSPECTION_DATE)
            .grower(UPDATED_GROWER)
            .packingDate(UPDATED_PACKING_DATE)
            .finalRecomendations(UPDATED_FINAL_RECOMENDATIONS);

        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspection))
            )
            .andExpect(status().isOk());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
        Inspection testInspection = inspectionList.get(inspectionList.size() - 1);
        assertThat(testInspection.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testInspection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInspection.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testInspection.getInspectionDate()).isEqualTo(UPDATED_INSPECTION_DATE);
        assertThat(testInspection.getGrower()).isEqualTo(UPDATED_GROWER);
        assertThat(testInspection.getPackingDate()).isEqualTo(UPDATED_PACKING_DATE);
        assertThat(testInspection.getObservations()).isEqualTo(DEFAULT_OBSERVATIONS);
        assertThat(testInspection.getFinalRecomendations()).isEqualTo(UPDATED_FINAL_RECOMENDATIONS);
        assertThat(testInspection.getExtraDetails()).isEqualTo(DEFAULT_EXTRA_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateInspectionWithPatch() throws Exception {
        // Initialize the database
        inspectionRepository.saveAndFlush(inspection);

        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();

        // Update the inspection using partial update
        Inspection partialUpdatedInspection = new Inspection();
        partialUpdatedInspection.setId(inspection.getId());

        partialUpdatedInspection
            .reportDate(UPDATED_REPORT_DATE)
            .description(UPDATED_DESCRIPTION)
            .place(UPDATED_PLACE)
            .inspectionDate(UPDATED_INSPECTION_DATE)
            .grower(UPDATED_GROWER)
            .packingDate(UPDATED_PACKING_DATE)
            .observations(UPDATED_OBSERVATIONS)
            .finalRecomendations(UPDATED_FINAL_RECOMENDATIONS)
            .extraDetails(UPDATED_EXTRA_DETAILS);

        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspection))
            )
            .andExpect(status().isOk());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
        Inspection testInspection = inspectionList.get(inspectionList.size() - 1);
        assertThat(testInspection.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testInspection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInspection.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testInspection.getInspectionDate()).isEqualTo(UPDATED_INSPECTION_DATE);
        assertThat(testInspection.getGrower()).isEqualTo(UPDATED_GROWER);
        assertThat(testInspection.getPackingDate()).isEqualTo(UPDATED_PACKING_DATE);
        assertThat(testInspection.getObservations()).isEqualTo(UPDATED_OBSERVATIONS);
        assertThat(testInspection.getFinalRecomendations()).isEqualTo(UPDATED_FINAL_RECOMENDATIONS);
        assertThat(testInspection.getExtraDetails()).isEqualTo(UPDATED_EXTRA_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();
        inspection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inspection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();
        inspection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();
        inspection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(inspection))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInspection() throws Exception {
        // Initialize the database
        inspectionRepository.saveAndFlush(inspection);

        int databaseSizeBeforeDelete = inspectionRepository.findAll().size();

        // Delete the inspection
        restInspectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, inspection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
