package ie.dbs.gymtrainingmanagement.WorkoutPlanItem;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WorkoutPlanItemForm {

    @NotNull(message = "An exercise must be selected")
    private Long exerciseId;

    @NotNull(message = "Sets is required")
    @Min(value = 1, message = "Sets must be at least 1")
    @Max(value = 20, message = "Sets can be at most 20")
    private Integer sets;

    @NotNull(message = "Reps is required")
    @Min(value = 1, message = "Reps must be at least 1")
    @Max(value = 100, message = "Reps can be at most 100")
    private Integer reps;

    // Optional
    @DecimalMin(value = "0.0", message = "Weight cannot be negative")
    @Digits(integer = 4, fraction = 2, message = "Weight can have up to 4 digits and 2 decimals")
    private BigDecimal weightKg;

    // Optional
    @Min(value = 0, message = "Rest cannot be negative")
    @Max(value = 1200, message = "Rest can be at most 1200 seconds")
    private Integer restSeconds;

    // Optional
    @Size(max = 30, message = "Day label can have at most 30 characters")
    private String dayLabel;

    // Optional. Empty means "add at the end" when creating, "keep as it is" when editing.
    @Min(value = 1, message = "Order must be at least 1")
    private Integer orderIndex;

    public WorkoutPlanItemForm() {
    }

    public WorkoutPlanItemForm(Long exerciseId, Integer sets, Integer reps, BigDecimal weightKg,
                               Integer restSeconds, String dayLabel, Integer orderIndex) {
        this.exerciseId = exerciseId;
        this.sets = sets;
        this.reps = reps;
        this.weightKg = weightKg;
        this.restSeconds = restSeconds;
        this.dayLabel = dayLabel;
        this.orderIndex = orderIndex;
    }
}
