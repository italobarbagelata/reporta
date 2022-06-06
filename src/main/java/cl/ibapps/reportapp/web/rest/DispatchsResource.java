package cl.ibapps.reportapp.web.rest;

import cl.ibapps.reportapp.domain.Dispatchs;
import cl.ibapps.reportapp.repository.DispatchsRepository;
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
 * REST controller for managing {@link cl.ibapps.reportapp.domain.Dispatchs}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DispatchsResource {

    private final Logger log = LoggerFactory.getLogger(DispatchsResource.class);

    private static final String ENTITY_NAME = "dispatchs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DispatchsRepository dispatchsRepository;

    public DispatchsResource(DispatchsRepository dispatchsRepository) {
        this.dispatchsRepository = dispatchsRepository;
    }

    /**
     * {@code POST  /dispatchs} : Create a new dispatchs.
     *
     * @param dispatchs the dispatchs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dispatchs, or with status {@code 400 (Bad Request)} if the dispatchs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dispatchs")
    public ResponseEntity<Dispatchs> createDispatchs(@RequestBody Dispatchs dispatchs) throws URISyntaxException {
        log.debug("REST request to save Dispatchs : {}", dispatchs);
        if (dispatchs.getId() != null) {
            throw new BadRequestAlertException("A new dispatchs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dispatchs result = dispatchsRepository.save(dispatchs);
        return ResponseEntity
            .created(new URI("/api/dispatchs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dispatchs/:id} : Updates an existing dispatchs.
     *
     * @param id the id of the dispatchs to save.
     * @param dispatchs the dispatchs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispatchs,
     * or with status {@code 400 (Bad Request)} if the dispatchs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dispatchs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dispatchs/{id}")
    public ResponseEntity<Dispatchs> updateDispatchs(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Dispatchs dispatchs
    ) throws URISyntaxException {
        log.debug("REST request to update Dispatchs : {}, {}", id, dispatchs);
        if (dispatchs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispatchs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispatchsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dispatchs result = dispatchsRepository.save(dispatchs);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dispatchs.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dispatchs/:id} : Partial updates given fields of an existing dispatchs, field will ignore if it is null
     *
     * @param id the id of the dispatchs to save.
     * @param dispatchs the dispatchs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispatchs,
     * or with status {@code 400 (Bad Request)} if the dispatchs is not valid,
     * or with status {@code 404 (Not Found)} if the dispatchs is not found,
     * or with status {@code 500 (Internal Server Error)} if the dispatchs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dispatchs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dispatchs> partialUpdateDispatchs(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Dispatchs dispatchs
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dispatchs partially : {}, {}", id, dispatchs);
        if (dispatchs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispatchs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispatchsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dispatchs> result = dispatchsRepository
            .findById(dispatchs.getId())
            .map(existingDispatchs -> {
                if (dispatchs.getDescription() != null) {
                    existingDispatchs.setDescription(dispatchs.getDescription());
                }

                return existingDispatchs;
            })
            .map(dispatchsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dispatchs.getId().toString())
        );
    }

    /**
     * {@code GET  /dispatchs} : get all the dispatchs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dispatchs in body.
     */
    @GetMapping("/dispatchs")
    public List<Dispatchs> getAllDispatchs() {
        log.debug("REST request to get all Dispatchs");
        return dispatchsRepository.findAll();
    }

    /**
     * {@code GET  /dispatchs/:id} : get the "id" dispatchs.
     *
     * @param id the id of the dispatchs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dispatchs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dispatchs/{id}")
    public ResponseEntity<Dispatchs> getDispatchs(@PathVariable Long id) {
        log.debug("REST request to get Dispatchs : {}", id);
        Optional<Dispatchs> dispatchs = dispatchsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dispatchs);
    }

    /**
     * {@code DELETE  /dispatchs/:id} : delete the "id" dispatchs.
     *
     * @param id the id of the dispatchs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dispatchs/{id}")
    public ResponseEntity<Void> deleteDispatchs(@PathVariable Long id) {
        log.debug("REST request to delete Dispatchs : {}", id);
        dispatchsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
