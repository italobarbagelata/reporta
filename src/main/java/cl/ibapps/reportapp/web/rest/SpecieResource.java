package cl.ibapps.reportapp.web.rest;

import cl.ibapps.reportapp.domain.Specie;
import cl.ibapps.reportapp.repository.SpecieRepository;
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
 * REST controller for managing {@link cl.ibapps.reportapp.domain.Specie}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SpecieResource {

    private final Logger log = LoggerFactory.getLogger(SpecieResource.class);

    private static final String ENTITY_NAME = "specie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecieRepository specieRepository;

    public SpecieResource(SpecieRepository specieRepository) {
        this.specieRepository = specieRepository;
    }

    /**
     * {@code POST  /species} : Create a new specie.
     *
     * @param specie the specie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specie, or with status {@code 400 (Bad Request)} if the specie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/species")
    public ResponseEntity<Specie> createSpecie(@RequestBody Specie specie) throws URISyntaxException {
        log.debug("REST request to save Specie : {}", specie);
        if (specie.getId() != null) {
            throw new BadRequestAlertException("A new specie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Specie result = specieRepository.save(specie);
        return ResponseEntity
            .created(new URI("/api/species/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /species/:id} : Updates an existing specie.
     *
     * @param id the id of the specie to save.
     * @param specie the specie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specie,
     * or with status {@code 400 (Bad Request)} if the specie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/species/{id}")
    public ResponseEntity<Specie> updateSpecie(@PathVariable(value = "id", required = false) final Long id, @RequestBody Specie specie)
        throws URISyntaxException {
        log.debug("REST request to update Specie : {}, {}", id, specie);
        if (specie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Specie result = specieRepository.save(specie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /species/:id} : Partial updates given fields of an existing specie, field will ignore if it is null
     *
     * @param id the id of the specie to save.
     * @param specie the specie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specie,
     * or with status {@code 400 (Bad Request)} if the specie is not valid,
     * or with status {@code 404 (Not Found)} if the specie is not found,
     * or with status {@code 500 (Internal Server Error)} if the specie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/species/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Specie> partialUpdateSpecie(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Specie specie
    ) throws URISyntaxException {
        log.debug("REST request to partial update Specie partially : {}, {}", id, specie);
        if (specie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Specie> result = specieRepository
            .findById(specie.getId())
            .map(existingSpecie -> {
                if (specie.getDescription() != null) {
                    existingSpecie.setDescription(specie.getDescription());
                }

                return existingSpecie;
            })
            .map(specieRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specie.getId().toString())
        );
    }

    /**
     * {@code GET  /species} : get all the species.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of species in body.
     */
    @GetMapping("/species")
    public List<Specie> getAllSpecies() {
        log.debug("REST request to get all Species");
        return specieRepository.findAll();
    }

    /**
     * {@code GET  /species/:id} : get the "id" specie.
     *
     * @param id the id of the specie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/species/{id}")
    public ResponseEntity<Specie> getSpecie(@PathVariable Long id) {
        log.debug("REST request to get Specie : {}", id);
        Optional<Specie> specie = specieRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(specie);
    }

    /**
     * {@code DELETE  /species/:id} : delete the "id" specie.
     *
     * @param id the id of the specie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/species/{id}")
    public ResponseEntity<Void> deleteSpecie(@PathVariable Long id) {
        log.debug("REST request to delete Specie : {}", id);
        specieRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
