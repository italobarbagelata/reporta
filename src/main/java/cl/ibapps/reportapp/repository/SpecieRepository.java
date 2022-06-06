package cl.ibapps.reportapp.repository;

import cl.ibapps.reportapp.domain.Specie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Specie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecieRepository extends JpaRepository<Specie, Long> {}
