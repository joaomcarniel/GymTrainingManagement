package ie.dbs.gymtrainingmanagement.WorkoutPlanItem;

import ie.dbs.gymtrainingmanagement.Exercise.Exercise;
import ie.dbs.gymtrainingmanagement.WorkoutPlan.WorkoutPlan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * One row of a workout plan, for example "Bench press, 3 sets of 10 reps, 60 kg, rest 90 s".
 */
@Entity
@Table(name = "workout_plan_items")
@Getter
@Setter
public class WorkoutPlanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "sets", nullable = false)
    private int sets;

    @Column(name = "reps", nullable = false)
    private int reps;

    // Optional: empty for bodyweight exercises
    @Column(name = "weight_kg", precision = 6, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "rest_seconds")
    private Integer restSeconds;

    // Position of the row inside the plan (1, 2, 3...)
    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    // Optional: lets one plan hold several training days ("Day A", "Day B", "Monday"...)
    @Column(name = "day_label", length = 30)
    private String dayLabel;

    public WorkoutPlanItem() {
    }

    public WorkoutPlanItem(WorkoutPlan workoutPlan, Exercise exercise, int sets, int reps,
                           BigDecimal weightKg, Integer restSeconds, int orderIndex, String dayLabel) {
        this.workoutPlan = workoutPlan;
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weightKg = weightKg;
        this.restSeconds = restSeconds;
        this.orderIndex = orderIndex;
        this.dayLabel = dayLabel;
    }
}
