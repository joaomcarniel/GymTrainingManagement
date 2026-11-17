package ie.dbs.gymtrainingmanagement.Equipment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class EquipmentForm {

    @NotBlank(message = "Equipment name is required")
    @Size(min = 2, max = 100,
            message = "Equipment name must contain between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description can have at most 500 characters")
    private String description;

    @NotNull(message = "A muscle group must be selected")
    private MuscleGroup muscleGroup;

    private MultipartFile image;

    public EquipmentForm() {
    }

    public EquipmentForm(String name, String description, MuscleGroup muscleGroup) {
        this.name = name;
        this.description = description;
        this.muscleGroup = muscleGroup;
    }
}
