package cl.ibapps.reportapp.web.rest;

import cl.ibapps.reportapp.domain.Variety;
import cl.ibapps.reportapp.repository.VarietyRepository;
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
 * REST controller for managing {@link cl.ibapps.reportapp.domain.Variety}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VarietyResource {

    private final Logger log = LoggerFactory.getLogger(VarietyResource.class);

    private static final String ENTITY_NAME = "variety";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VarietyRepository varietyRepository;

    public VarietyResource(VarietyRepository varietyRepository) {
        this.varietyRepository = varietyRepository;
    }

    /**
     * {@code POST  /varieties} : Create a new variety.
     *
     * @param variety the variety to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new variety, or with status {@code 400 (Bad Request)} if the variety has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/varieties")
    public ResponseEntity<Variety> createVariety(@RequestBody Variety variety) throws URISyntaxException {
        log.debug("REST request to save Variety : {}", variety);
        if (variety.getId() != null) {
            throw new BadRequestAlertException("A new variety cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Variety result = varietyRepository.save(variety);
        return ResponseEntity
            .created(new URI("/api/varieties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /varieties/:id} : Updates an existing variety.
     *
     * @param id the id of the variety to save.
     * @param variety the variety to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated variety,
     * or with status {@code 400 (Bad Request)} if the variety is not valid,
     * or with status {@code 500 (Internal Server Error)} if the variety couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/varieties/{id}")
    public ResponseEntity<Variety> updateVariety(@PathVariable(value = "id", required = false) final Long id, @RequestBody Variety variety)
        throws URISyntaxException {
        log.debug("REST request to update Variety : {}, {}", id, variety);
        if (variety.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, variety.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!varietyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Variety result = varietyRepository.save(variety);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, variety.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /varieties/:id} : Partial updates given fields of an existing variety, field will ignore if it is null
     *
     * @param id the id of the variety to save.
     * @param variety the variety to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated variety,
     * or with status {@code 400 (Bad Request)} if the variety is not valid,
     * or with status {@code 404 (Not Found)} if the variety is not found,
     * or with status {@code 500 (Internal Server Error)} if the variety couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/varieties/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Variety> partialUpdateVariety(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Variety variety
    ) throws URISyntaxException {
        log.debug("REST request to partial update Variety partially : {}, {}", id, variety);
        if (variety.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, variety.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!varietyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Variety> result = varietyRepository
            .findById(variety.getId())
            .map(existingVariety -> {
                if (variety.getDescription() != null) {
                    existingVariety.setDescription(variety.getDescription());
                }

                return existingVariety;
            })
            .map(varietyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, variety.getId().toString())
        );
    }

    /**
     * {@code GET  /varieties} : get all the varieties.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of varieties in body.
     */
    @GetMapping("/varieties")
    public List<Variety> getAllVarieties() {
        log.debug("REST request to get all Varieties");
        return varietyRepository.findAll();
    }

    /**
     * {@code GET  /varieties/:id} : get the "id" variety.
     *
     * @param id the id of the variety to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the variety, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/varieties/{id}")
    public ResponseEntity<Variety> getVariety(@PathVariable Long id) {
        log.debug("REST request to get Variety : {}", id);
        Optional<Variety> variety = varietyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(variety);
    }

    /**
     * {@code DELETE  /varieties/:id} : delete the "id" variety.
     *
     * @param id the id of the variety to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/varieties/{id}")
    public ResponseEntity<Void> deleteVariety(@PathVariable Long id) {
        log.debug("REST request to delete Variety : {}", id);
        varietyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
