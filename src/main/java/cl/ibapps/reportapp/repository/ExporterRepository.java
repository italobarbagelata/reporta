package cl.ibapps.reportapp.repository;

import cl.ibapps.reportapp.domain.Exporter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Exporter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExporterRepository extends JpaRepository<Exporter, Long> {}
