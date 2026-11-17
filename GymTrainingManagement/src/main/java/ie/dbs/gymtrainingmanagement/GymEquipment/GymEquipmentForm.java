package ie.dbs.gymtrainingmanagement.GymEquipment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GymEquipmentForm {

    @NotNull(message = "A gym must be selected")
    private Long gymId;

    @NotNull(message = "An equipment must be selected")
    private Long equipmentId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 999, message = "Quantity can be at most 999")
    private Integer quantity;

    @NotNull(message = "A condition must be selected")
    private EquipmentCondition condition;

    public GymEquipmentForm() {
    }

    public GymEquipmentForm(Long gymId, Long equipmentId, Integer quantity, EquipmentCondition condition) {
        this.gymId = gymId;
        this.equipmentId = equipmentId;
        this.quantity = quantity;
        this.condition = condition;
    }
}
