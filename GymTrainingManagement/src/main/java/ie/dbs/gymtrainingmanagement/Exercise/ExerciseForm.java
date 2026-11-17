package ie.dbs.gymtrainingmanagement.Exercise;

import ie.dbs.gymtrainingmanagement.Equipment.MuscleGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseForm {

    @NotBlank(message = "Exercise name is required")
    @Size(min = 2, max = 100,
            message = "Exercise name must contain between 2 and 100 characters")
    private String name;

    @NotNull(message = "A muscle group must be selected")
    private MuscleGroup muscleGroup;

    // Optional
    private Long equipmentId;

    @Size(max = 500, message = "Description can have at most 500 characters")
    private String description;

    public ExerciseForm() {
    }

    public ExerciseForm(String name, MuscleGroup muscleGroup, Long equipmentId, String description) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.equipmentId = equipmentId;
        this.description = description;
    }
}
