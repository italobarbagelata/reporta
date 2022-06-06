package cl.ibapps.reportapp.repository;

import cl.ibapps.reportapp.domain.Packages;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Packages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackagesRepository extends JpaRepository<Packages, Long> {}
