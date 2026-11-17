package ie.dbs.gymtrainingmanagement.WorkoutPlanItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutPlanItemRepository extends JpaRepository<WorkoutPlanItem, Long> {

    @Query("select i from WorkoutPlanItem i join fetch i.exercise e " +
            "where i.workoutPlan.id = :planId order by i.orderIndex, i.id")
    List<WorkoutPlanItem> findByPlanIdWithDetails(@Param("planId") Long planId);

    // Highest order of the plan, or 0 when the plan has no rows yet
    @Query("select coalesce(max(i.orderIndex), 0) from WorkoutPlanItem i where i.workoutPlan.id = :planId")
    Integer findMaxOrderIndex(@Param("planId") Long planId);

    @Modifying
    @Query("delete from WorkoutPlanItem i where i.workoutPlan.id = :planId")
    void deleteAllByWorkoutPlanId(@Param("planId") Long planId);
}
