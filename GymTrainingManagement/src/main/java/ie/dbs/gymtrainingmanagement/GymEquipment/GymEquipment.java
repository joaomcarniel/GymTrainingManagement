package ie.dbs.gymtrainingmanagement.GymEquipment;

import ie.dbs.gymtrainingmanagement.Equipment.Equipment;
import ie.dbs.gymtrainingmanagement.Gym.Gym;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Join between Gym and Equipment that also carries data:
 * how many units the gym has and in which condition.
 */
@Entity
@Table(name = "gym_equipments",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_gym_equipment", columnNames = {"gym_id", "equipment_id"}))
@Getter
@Setter
public class GymEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @ManyToOne(optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_condition", nullable = false, length = 30)
    private EquipmentCondition condition;

    public GymEquipment() {
    }

    public GymEquipment(Gym gym, Equipment equipment, int quantity, EquipmentCondition condition) {
        this.gym = gym;
        this.equipment = equipment;
        this.quantity = quantity;
        this.condition = condition;
    }
}
