package cl.ibapps.reportapp.repository;

import cl.ibapps.reportapp.domain.FinalOverall;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FinalOverall entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinalOverallRepository extends JpaRepository<FinalOverall, Long> {}
