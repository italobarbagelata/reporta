package cl.ibapps.reportapp.repository;

import cl.ibapps.reportapp.domain.Variety;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Variety entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VarietyRepository extends JpaRepository<Variety, Long> {}
