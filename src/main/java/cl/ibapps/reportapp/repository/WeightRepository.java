package cl.ibapps.reportapp.repository;

import cl.ibapps.reportapp.domain.Weight;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Weight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeightRepository extends JpaRepository<Weight, Long> {}
