package cl.ibapps.reportapp.repository;

import cl.ibapps.reportapp.domain.Color;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Color entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {}
