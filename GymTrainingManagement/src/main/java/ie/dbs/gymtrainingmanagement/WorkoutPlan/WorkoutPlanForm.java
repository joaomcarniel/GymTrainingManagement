package ie.dbs.gymtrainingmanagement.WorkoutPlan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class WorkoutPlanForm {

    @NotBlank(message = "Plan name is required")
    @Size(min = 2, max = 100,
            message = "Plan name must contain between 2 and 100 characters")
    private String name;

    @NotNull(message = "A member must be selected")
    private Long memberId;

    @NotNull(message = "Start date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Size(max = 500, message = "Notes can have at most 500 characters")
    private String notes;

    private boolean active = true;

    public WorkoutPlanForm() {
    }

    public WorkoutPlanForm(String name, Long memberId, LocalDate startDate, String notes, boolean active) {
        this.name = name;
        this.memberId = memberId;
        this.startDate = startDate;
        this.notes = notes;
        this.active = active;
    }
}
