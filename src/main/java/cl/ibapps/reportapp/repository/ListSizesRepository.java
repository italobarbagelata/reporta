package cl.ibapps.reportapp.repository;

import cl.ibapps.reportapp.domain.ListSizes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ListSizes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ListSizesRepository extends JpaRepository<ListSizes, Long> {}
