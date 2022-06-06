package cl.ibapps.reportapp.repository;

import cl.ibapps.reportapp.domain.Inspection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Inspection entity.
 */
@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long> {
    @Query("select inspection from Inspection inspection where inspection.user.login = ?#{principal.username}")
    List<Inspection> findByUserIsCurrentUser();

    default Optional<Inspection> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Inspection> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Inspection> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct inspection from Inspection inspection left join fetch inspection.user left join fetch inspection.label left join fetch inspection.exporter left join fetch inspection.phytoChina left join fetch inspection.dispatchs left join fetch inspection.packages left join fetch inspection.weight left join fetch inspection.specie left join fetch inspection.variety left join fetch inspection.color left join fetch inspection.finalOverall left join fetch inspection.listSizes",
        countQuery = "select count(distinct inspection) from Inspection inspection"
    )
    Page<Inspection> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct inspection from Inspection inspection left join fetch inspection.user left join fetch inspection.label left join fetch inspection.exporter left join fetch inspection.phytoChina left join fetch inspection.dispatchs left join fetch inspection.packages left join fetch inspection.weight left join fetch inspection.specie left join fetch inspection.variety left join fetch inspection.color left join fetch inspection.finalOverall left join fetch inspection.listSizes"
    )
    List<Inspection> findAllWithToOneRelationships();

    @Query(
        "select inspection from Inspection inspection left join fetch inspection.user left join fetch inspection.label left join fetch inspection.exporter left join fetch inspection.phytoChina left join fetch inspection.dispatchs left join fetch inspection.packages left join fetch inspection.weight left join fetch inspection.specie left join fetch inspection.variety left join fetch inspection.color left join fetch inspection.finalOverall left join fetch inspection.listSizes where inspection.id =:id"
    )
    Optional<Inspection> findOneWithToOneRelationships(@Param("id") Long id);
}
