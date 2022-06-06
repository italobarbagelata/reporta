package cl.ibapps.reportapp.web.rest;

import cl.ibapps.reportapp.domain.FinalOverall;
import cl.ibapps.reportapp.repository.FinalOverallRepository;
import cl.ibapps.reportapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link cl.ibapps.reportapp.domain.FinalOverall}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FinalOverallResource {

    private final Logger log = LoggerFactory.getLogger(FinalOverallResource.class);

    private static final String ENTITY_NAME = "finalOverall";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinalOverallRepository finalOverallRepository;

    public FinalOverallResource(FinalOverallRepository finalOverallRepository) {
        this.finalOverallRepository = finalOverallRepository;
    }

    /**
     * {@code POST  /final-overalls} : Create a new finalOverall.
     *
     * @param finalOverall the finalOverall to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new finalOverall, or with status {@code 400 (Bad Request)} if the finalOverall has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/final-overalls")
    public ResponseEntity<FinalOverall> createFinalOverall(@RequestBody FinalOverall finalOverall) throws URISyntaxException {
        log.debug("REST request to save FinalOverall : {}", finalOverall);
        if (finalOverall.getId() != null) {
            throw new BadRequestAlertException("A new finalOverall cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FinalOverall result = finalOverallRepository.save(finalOverall);
        return ResponseEntity
            .created(new URI("/api/final-overalls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /final-overalls/:id} : Updates an existing finalOverall.
     *
     * @param id the id of the finalOverall to save.
     * @param finalOverall the finalOverall to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated finalOverall,
     * or with status {@code 400 (Bad Request)} if the finalOverall is not valid,
     * or with status {@code 500 (Internal Server Error)} if the finalOverall couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/final-overalls/{id}")
    public ResponseEntity<FinalOverall> updateFinalOverall(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FinalOverall finalOverall
    ) throws URISyntaxException {
        log.debug("REST request to update FinalOverall : {}, {}", id, finalOverall);
        if (finalOverall.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, finalOverall.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!finalOverallRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FinalOverall result = finalOverallRepository.save(finalOverall);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, finalOverall.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /final-overalls/:id} : Partial updates given fields of an existing finalOverall, field will ignore if it is null
     *
     * @param id the id of the finalOverall to save.
     * @param finalOverall the finalOverall to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated finalOverall,
     * or with status {@code 400 (Bad Request)} if the finalOverall is not valid,
     * or with status {@code 404 (Not Found)} if the finalOverall is not found,
     * or with status {@code 500 (Internal Server Error)} if the finalOverall couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/final-overalls/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FinalOverall> partialUpdateFinalOverall(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FinalOverall finalOverall
    ) throws URISyntaxException {
        log.debug("REST request to partial update FinalOverall partially : {}, {}", id, finalOverall);
        if (finalOverall.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, finalOverall.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!finalOverallRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FinalOverall> result = finalOverallRepository
            .findById(finalOverall.getId())
            .map(existingFinalOverall -> {
                if (finalOverall.getNote() != null) {
                    existingFinalOverall.setNote(finalOverall.getNote());
                }
                if (finalOverall.getDescription() != null) {
                    existingFinalOverall.setDescription(finalOverall.getDescription());
                }

                return existingFinalOverall;
            })
            .map(finalOverallRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, finalOverall.getId().toString())
        );
    }

    /**
     * {@code GET  /final-overalls} : get all the finalOveralls.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of finalOveralls in body.
     */
    @GetMapping("/final-overalls")
    public List<FinalOverall> getAllFinalOveralls() {
        log.debug("REST request to get all FinalOveralls");
        return finalOverallRepository.findAll();
    }

    /**
     * {@code GET  /final-overalls/:id} : get the "id" finalOverall.
     *
     * @param id the id of the finalOverall to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the finalOverall, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/final-overalls/{id}")
    public ResponseEntity<FinalOverall> getFinalOverall(@PathVariable Long id) {
        log.debug("REST request to get FinalOverall : {}", id);
        Optional<FinalOverall> finalOverall = finalOverallRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(finalOverall);
    }

    /**
     * {@code DELETE  /final-overalls/:id} : delete the "id" finalOverall.
     *
     * @param id the id of the finalOverall to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/final-overalls/{id}")
    public ResponseEntity<Void> deleteFinalOverall(@PathVariable Long id) {
        log.debug("REST request to delete FinalOverall : {}", id);
        finalOverallRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
