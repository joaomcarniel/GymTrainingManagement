package ie.dbs.gymtrainingmanagement.Exercise;

import ie.dbs.gymtrainingmanagement.Equipment.Equipment;
import ie.dbs.gymtrainingmanagement.Equipment.MuscleGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "exercises")
@Getter
@Setter
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "muscle_group", nullable = false, length = 30)
    private MuscleGroup muscleGroup;

    // Optional: bodyweight exercises (push-ups, planks) do not need equipment.
    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    public Exercise() {
    }

    public Exercise(String name, String description, MuscleGroup muscleGroup, Equipment equipment) {
        this.name = name;
        this.description = description;
        this.muscleGroup = muscleGroup;
        this.equipment = equipment;
    }
}
