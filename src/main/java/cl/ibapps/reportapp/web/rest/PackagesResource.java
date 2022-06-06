package cl.ibapps.reportapp.web.rest;

import cl.ibapps.reportapp.domain.Packages;
import cl.ibapps.reportapp.repository.PackagesRepository;
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
 * REST controller for managing {@link cl.ibapps.reportapp.domain.Packages}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PackagesResource {

    private final Logger log = LoggerFactory.getLogger(PackagesResource.class);

    private static final String ENTITY_NAME = "packages";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PackagesRepository packagesRepository;

    public PackagesResource(PackagesRepository packagesRepository) {
        this.packagesRepository = packagesRepository;
    }

    /**
     * {@code POST  /packages} : Create a new packages.
     *
     * @param packages the packages to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new packages, or with status {@code 400 (Bad Request)} if the packages has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/packages")
    public ResponseEntity<Packages> createPackages(@RequestBody Packages packages) throws URISyntaxException {
        log.debug("REST request to save Packages : {}", packages);
        if (packages.getId() != null) {
            throw new BadRequestAlertException("A new packages cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Packages result = packagesRepository.save(packages);
        return ResponseEntity
            .created(new URI("/api/packages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /packages/:id} : Updates an existing packages.
     *
     * @param id the id of the packages to save.
     * @param packages the packages to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packages,
     * or with status {@code 400 (Bad Request)} if the packages is not valid,
     * or with status {@code 500 (Internal Server Error)} if the packages couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/packages/{id}")
    public ResponseEntity<Packages> updatePackages(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Packages packages
    ) throws URISyntaxException {
        log.debug("REST request to update Packages : {}, {}", id, packages);
        if (packages.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packages.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Packages result = packagesRepository.save(packages);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packages.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /packages/:id} : Partial updates given fields of an existing packages, field will ignore if it is null
     *
     * @param id the id of the packages to save.
     * @param packages the packages to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packages,
     * or with status {@code 400 (Bad Request)} if the packages is not valid,
     * or with status {@code 404 (Not Found)} if the packages is not found,
     * or with status {@code 500 (Internal Server Error)} if the packages couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/packages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Packages> partialUpdatePackages(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Packages packages
    ) throws URISyntaxException {
        log.debug("REST request to partial update Packages partially : {}, {}", id, packages);
        if (packages.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packages.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Packages> result = packagesRepository
            .findById(packages.getId())
            .map(existingPackages -> {
                if (packages.getDescription() != null) {
                    existingPackages.setDescription(packages.getDescription());
                }

                return existingPackages;
            })
            .map(packagesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packages.getId().toString())
        );
    }

    /**
     * {@code GET  /packages} : get all the packages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packages in body.
     */
    @GetMapping("/packages")
    public List<Packages> getAllPackages() {
        log.debug("REST request to get all Packages");
        return packagesRepository.findAll();
    }

    /**
     * {@code GET  /packages/:id} : get the "id" packages.
     *
     * @param id the id of the packages to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the packages, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/packages/{id}")
    public ResponseEntity<Packages> getPackages(@PathVariable Long id) {
        log.debug("REST request to get Packages : {}", id);
        Optional<Packages> packages = packagesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(packages);
    }

    /**
     * {@code DELETE  /packages/:id} : delete the "id" packages.
     *
     * @param id the id of the packages to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/packages/{id}")
    public ResponseEntity<Void> deletePackages(@PathVariable Long id) {
        log.debug("REST request to delete Packages : {}", id);
        packagesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
