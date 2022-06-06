package cl.ibapps.reportapp.repository;

import cl.ibapps.reportapp.domain.PhytoChina;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PhytoChina entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhytoChinaRepository extends JpaRepository<PhytoChina, Long> {}
