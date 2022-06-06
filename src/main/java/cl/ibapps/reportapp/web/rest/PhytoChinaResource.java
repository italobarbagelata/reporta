package cl.ibapps.reportapp.web.rest;

import cl.ibapps.reportapp.domain.PhytoChina;
import cl.ibapps.reportapp.repository.PhytoChinaRepository;
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
 * REST controller for managing {@link cl.ibapps.reportapp.domain.PhytoChina}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PhytoChinaResource {

    private final Logger log = LoggerFactory.getLogger(PhytoChinaResource.class);

    private static final String ENTITY_NAME = "phytoChina";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhytoChinaRepository phytoChinaRepository;

    public PhytoChinaResource(PhytoChinaRepository phytoChinaRepository) {
        this.phytoChinaRepository = phytoChinaRepository;
    }

    /**
     * {@code POST  /phyto-chinas} : Create a new phytoChina.
     *
     * @param phytoChina the phytoChina to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phytoChina, or with status {@code 400 (Bad Request)} if the phytoChina has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/phyto-chinas")
    public ResponseEntity<PhytoChina> createPhytoChina(@RequestBody PhytoChina phytoChina) throws URISyntaxException {
        log.debug("REST request to save PhytoChina : {}", phytoChina);
        if (phytoChina.getId() != null) {
            throw new BadRequestAlertException("A new phytoChina cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PhytoChina result = phytoChinaRepository.save(phytoChina);
        return ResponseEntity
            .created(new URI("/api/phyto-chinas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /phyto-chinas/:id} : Updates an existing phytoChina.
     *
     * @param id the id of the phytoChina to save.
     * @param phytoChina the phytoChina to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phytoChina,
     * or with status {@code 400 (Bad Request)} if the phytoChina is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phytoChina couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/phyto-chinas/{id}")
    public ResponseEntity<PhytoChina> updatePhytoChina(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PhytoChina phytoChina
    ) throws URISyntaxException {
        log.debug("REST request to update PhytoChina : {}, {}", id, phytoChina);
        if (phytoChina.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phytoChina.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phytoChinaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PhytoChina result = phytoChinaRepository.save(phytoChina);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phytoChina.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /phyto-chinas/:id} : Partial updates given fields of an existing phytoChina, field will ignore if it is null
     *
     * @param id the id of the phytoChina to save.
     * @param phytoChina the phytoChina to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phytoChina,
     * or with status {@code 400 (Bad Request)} if the phytoChina is not valid,
     * or with status {@code 404 (Not Found)} if the phytoChina is not found,
     * or with status {@code 500 (Internal Server Error)} if the phytoChina couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/phyto-chinas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PhytoChina> partialUpdatePhytoChina(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PhytoChina phytoChina
    ) throws URISyntaxException {
        log.debug("REST request to partial update PhytoChina partially : {}, {}", id, phytoChina);
        if (phytoChina.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phytoChina.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phytoChinaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PhytoChina> result = phytoChinaRepository
            .findById(phytoChina.getId())
            .map(existingPhytoChina -> {
                if (phytoChina.getDescription() != null) {
                    existingPhytoChina.setDescription(phytoChina.getDescription());
                }

                return existingPhytoChina;
            })
            .map(phytoChinaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phytoChina.getId().toString())
        );
    }

    /**
     * {@code GET  /phyto-chinas} : get all the phytoChinas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phytoChinas in body.
     */
    @GetMapping("/phyto-chinas")
    public List<PhytoChina> getAllPhytoChinas() {
        log.debug("REST request to get all PhytoChinas");
        return phytoChinaRepository.findAll();
    }

    /**
     * {@code GET  /phyto-chinas/:id} : get the "id" phytoChina.
     *
     * @param id the id of the phytoChina to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phytoChina, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/phyto-chinas/{id}")
    public ResponseEntity<PhytoChina> getPhytoChina(@PathVariable Long id) {
        log.debug("REST request to get PhytoChina : {}", id);
        Optional<PhytoChina> phytoChina = phytoChinaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(phytoChina);
    }

    /**
     * {@code DELETE  /phyto-chinas/:id} : delete the "id" phytoChina.
     *
     * @param id the id of the phytoChina to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/phyto-chinas/{id}")
    public ResponseEntity<Void> deletePhytoChina(@PathVariable Long id) {
        log.debug("REST request to delete PhytoChina : {}", id);
        phytoChinaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
