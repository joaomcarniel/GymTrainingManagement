package ie.dbs.gymtrainingmanagement.GymEquipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymEquipmentRepository extends JpaRepository<GymEquipment, Long> {

    // "join fetch" loads gym and equipment in the same query, so the list page does not run one query per row.
    @Query("select ge from GymEquipment ge join fetch ge.gym g join fetch ge.equipment e " +
            "order by g.name, e.name")
    List<GymEquipment> findAllWithDetails();

    @Query("select ge from GymEquipment ge join fetch ge.gym g join fetch ge.equipment e " +
            "where g.id = :gymId order by e.name")
    List<GymEquipment> findByGymIdWithDetails(@Param("gymId") Long gymId);

    boolean existsByGymIdAndEquipmentId(Long gymId, Long equipmentId);
}
