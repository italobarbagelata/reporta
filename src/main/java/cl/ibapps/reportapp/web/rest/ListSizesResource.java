package cl.ibapps.reportapp.web.rest;

import cl.ibapps.reportapp.domain.ListSizes;
import cl.ibapps.reportapp.repository.ListSizesRepository;
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
 * REST controller for managing {@link cl.ibapps.reportapp.domain.ListSizes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ListSizesResource {

    private final Logger log = LoggerFactory.getLogger(ListSizesResource.class);

    private static final String ENTITY_NAME = "listSizes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ListSizesRepository listSizesRepository;

    public ListSizesResource(ListSizesRepository listSizesRepository) {
        this.listSizesRepository = listSizesRepository;
    }

    /**
     * {@code POST  /list-sizes} : Create a new listSizes.
     *
     * @param listSizes the listSizes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new listSizes, or with status {@code 400 (Bad Request)} if the listSizes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/list-sizes")
    public ResponseEntity<ListSizes> createListSizes(@RequestBody ListSizes listSizes) throws URISyntaxException {
        log.debug("REST request to save ListSizes : {}", listSizes);
        if (listSizes.getId() != null) {
            throw new BadRequestAlertException("A new listSizes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ListSizes result = listSizesRepository.save(listSizes);
        return ResponseEntity
            .created(new URI("/api/list-sizes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /list-sizes/:id} : Updates an existing listSizes.
     *
     * @param id the id of the listSizes to save.
     * @param listSizes the listSizes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated listSizes,
     * or with status {@code 400 (Bad Request)} if the listSizes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the listSizes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/list-sizes/{id}")
    public ResponseEntity<ListSizes> updateListSizes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ListSizes listSizes
    ) throws URISyntaxException {
        log.debug("REST request to update ListSizes : {}, {}", id, listSizes);
        if (listSizes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, listSizes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!listSizesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ListSizes result = listSizesRepository.save(listSizes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, listSizes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /list-sizes/:id} : Partial updates given fields of an existing listSizes, field will ignore if it is null
     *
     * @param id the id of the listSizes to save.
     * @param listSizes the listSizes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated listSizes,
     * or with status {@code 400 (Bad Request)} if the listSizes is not valid,
     * or with status {@code 404 (Not Found)} if the listSizes is not found,
     * or with status {@code 500 (Internal Server Error)} if the listSizes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/list-sizes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ListSizes> partialUpdateListSizes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ListSizes listSizes
    ) throws URISyntaxException {
        log.debug("REST request to partial update ListSizes partially : {}, {}", id, listSizes);
        if (listSizes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, listSizes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!listSizesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ListSizes> result = listSizesRepository
            .findById(listSizes.getId())
            .map(existingListSizes -> {
                if (listSizes.getDescription() != null) {
                    existingListSizes.setDescription(listSizes.getDescription());
                }

                return existingListSizes;
            })
            .map(listSizesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, listSizes.getId().toString())
        );
    }

    /**
     * {@code GET  /list-sizes} : get all the listSizes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of listSizes in body.
     */
    @GetMapping("/list-sizes")
    public List<ListSizes> getAllListSizes() {
        log.debug("REST request to get all ListSizes");
        return listSizesRepository.findAll();
    }

    /**
     * {@code GET  /list-sizes/:id} : get the "id" listSizes.
     *
     * @param id the id of the listSizes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the listSizes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/list-sizes/{id}")
    public ResponseEntity<ListSizes> getListSizes(@PathVariable Long id) {
        log.debug("REST request to get ListSizes : {}", id);
        Optional<ListSizes> listSizes = listSizesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(listSizes);
    }

    /**
     * {@code DELETE  /list-sizes/:id} : delete the "id" listSizes.
     *
     * @param id the id of the listSizes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/list-sizes/{id}")
    public ResponseEntity<Void> deleteListSizes(@PathVariable Long id) {
        log.debug("REST request to delete ListSizes : {}", id);
        listSizesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
