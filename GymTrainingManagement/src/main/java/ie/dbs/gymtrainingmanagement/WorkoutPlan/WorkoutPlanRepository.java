package ie.dbs.gymtrainingmanagement.WorkoutPlan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

    @Query("select p from WorkoutPlan p join fetch p.member m " +
            "order by p.startDate desc, p.name")
    List<WorkoutPlan> findAllWithDetails();

    @Query("select p from WorkoutPlan p join fetch p.member m " +
            "where m.id = :memberId order by p.startDate desc, p.name")
    List<WorkoutPlan> findByMemberIdWithDetails(@Param("memberId") Long memberId);
}
