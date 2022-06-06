package cl.ibapps.reportapp.repository;

import cl.ibapps.reportapp.domain.Dispatchs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dispatchs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DispatchsRepository extends JpaRepository<Dispatchs, Long> {}
