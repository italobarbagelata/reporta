package cl.ibapps.reportapp.web.rest;

import cl.ibapps.reportapp.domain.Exporter;
import cl.ibapps.reportapp.repository.ExporterRepository;
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
 * REST controller for managing {@link cl.ibapps.reportapp.domain.Exporter}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExporterResource {

    private final Logger log = LoggerFactory.getLogger(ExporterResource.class);

    private static final String ENTITY_NAME = "exporter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExporterRepository exporterRepository;

    public ExporterResource(ExporterRepository exporterRepository) {
        this.exporterRepository = exporterRepository;
    }

    /**
     * {@code POST  /exporters} : Create a new exporter.
     *
     * @param exporter the exporter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exporter, or with status {@code 400 (Bad Request)} if the exporter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exporters")
    public ResponseEntity<Exporter> createExporter(@RequestBody Exporter exporter) throws URISyntaxException {
        log.debug("REST request to save Exporter : {}", exporter);
        if (exporter.getId() != null) {
            throw new BadRequestAlertException("A new exporter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Exporter result = exporterRepository.save(exporter);
        return ResponseEntity
            .created(new URI("/api/exporters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exporters/:id} : Updates an existing exporter.
     *
     * @param id the id of the exporter to save.
     * @param exporter the exporter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exporter,
     * or with status {@code 400 (Bad Request)} if the exporter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exporter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exporters/{id}")
    public ResponseEntity<Exporter> updateExporter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Exporter exporter
    ) throws URISyntaxException {
        log.debug("REST request to update Exporter : {}, {}", id, exporter);
        if (exporter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exporter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exporterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Exporter result = exporterRepository.save(exporter);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exporter.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exporters/:id} : Partial updates given fields of an existing exporter, field will ignore if it is null
     *
     * @param id the id of the exporter to save.
     * @param exporter the exporter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exporter,
     * or with status {@code 400 (Bad Request)} if the exporter is not valid,
     * or with status {@code 404 (Not Found)} if the exporter is not found,
     * or with status {@code 500 (Internal Server Error)} if the exporter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exporters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Exporter> partialUpdateExporter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Exporter exporter
    ) throws URISyntaxException {
        log.debug("REST request to partial update Exporter partially : {}, {}", id, exporter);
        if (exporter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exporter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exporterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Exporter> result = exporterRepository
            .findById(exporter.getId())
            .map(existingExporter -> {
                if (exporter.getDescription() != null) {
                    existingExporter.setDescription(exporter.getDescription());
                }

                return existingExporter;
            })
            .map(exporterRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exporter.getId().toString())
        );
    }

    /**
     * {@code GET  /exporters} : get all the exporters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exporters in body.
     */
    @GetMapping("/exporters")
    public List<Exporter> getAllExporters() {
        log.debug("REST request to get all Exporters");
        return exporterRepository.findAll();
    }

    /**
     * {@code GET  /exporters/:id} : get the "id" exporter.
     *
     * @param id the id of the exporter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exporter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exporters/{id}")
    public ResponseEntity<Exporter> getExporter(@PathVariable Long id) {
        log.debug("REST request to get Exporter : {}", id);
        Optional<Exporter> exporter = exporterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(exporter);
    }

    /**
     * {@code DELETE  /exporters/:id} : delete the "id" exporter.
     *
     * @param id the id of the exporter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exporters/{id}")
    public ResponseEntity<Void> deleteExporter(@PathVariable Long id) {
        log.debug("REST request to delete Exporter : {}", id);
        exporterRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
