package cl.ibapps.reportapp.web.rest;

import cl.ibapps.reportapp.domain.Color;
import cl.ibapps.reportapp.repository.ColorRepository;
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
 * REST controller for managing {@link cl.ibapps.reportapp.domain.Color}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ColorResource {

    private final Logger log = LoggerFactory.getLogger(ColorResource.class);

    private static final String ENTITY_NAME = "color";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ColorRepository colorRepository;

    public ColorResource(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    /**
     * {@code POST  /colors} : Create a new color.
     *
     * @param color the color to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new color, or with status {@code 400 (Bad Request)} if the color has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/colors")
    public ResponseEntity<Color> createColor(@RequestBody Color color) throws URISyntaxException {
        log.debug("REST request to save Color : {}", color);
        if (color.getId() != null) {
            throw new BadRequestAlertException("A new color cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Color result = colorRepository.save(color);
        return ResponseEntity
            .created(new URI("/api/colors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /colors/:id} : Updates an existing color.
     *
     * @param id the id of the color to save.
     * @param color the color to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated color,
     * or with status {@code 400 (Bad Request)} if the color is not valid,
     * or with status {@code 500 (Internal Server Error)} if the color couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/colors/{id}")
    public ResponseEntity<Color> updateColor(@PathVariable(value = "id", required = false) final Long id, @RequestBody Color color)
        throws URISyntaxException {
        log.debug("REST request to update Color : {}, {}", id, color);
        if (color.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, color.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!colorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Color result = colorRepository.save(color);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, color.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /colors/:id} : Partial updates given fields of an existing color, field will ignore if it is null
     *
     * @param id the id of the color to save.
     * @param color the color to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated color,
     * or with status {@code 400 (Bad Request)} if the color is not valid,
     * or with status {@code 404 (Not Found)} if the color is not found,
     * or with status {@code 500 (Internal Server Error)} if the color couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/colors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Color> partialUpdateColor(@PathVariable(value = "id", required = false) final Long id, @RequestBody Color color)
        throws URISyntaxException {
        log.debug("REST request to partial update Color partially : {}, {}", id, color);
        if (color.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, color.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!colorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Color> result = colorRepository
            .findById(color.getId())
            .map(existingColor -> {
                if (color.getDescription() != null) {
                    existingColor.setDescription(color.getDescription());
                }

                return existingColor;
            })
            .map(colorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, color.getId().toString())
        );
    }

    /**
     * {@code GET  /colors} : get all the colors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of colors in body.
     */
    @GetMapping("/colors")
    public List<Color> getAllColors() {
        log.debug("REST request to get all Colors");
        return colorRepository.findAll();
    }

    /**
     * {@code GET  /colors/:id} : get the "id" color.
     *
     * @param id the id of the color to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the color, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/colors/{id}")
    public ResponseEntity<Color> getColor(@PathVariable Long id) {
        log.debug("REST request to get Color : {}", id);
        Optional<Color> color = colorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(color);
    }

    /**
     * {@code DELETE  /colors/:id} : delete the "id" color.
     *
     * @param id the id of the color to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/colors/{id}")
    public ResponseEntity<Void> deleteColor(@PathVariable Long id) {
        log.debug("REST request to delete Color : {}", id);
        colorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
