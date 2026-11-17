package ie.dbs.gymtrainingmanagement.Equipment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "equipments")
@Getter
@Setter
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "muscle_group", nullable = false, length = 30)
    private MuscleGroup muscleGroup;

    @Column(name = "image_path")
    private String imagePath;

    public Equipment() {
    }

    public Equipment(String name, String description, MuscleGroup muscleGroup, String imagePath) {
        this.name = name;
        this.description = description;
        this.muscleGroup = muscleGroup;
        this.imagePath = imagePath;
    }
}
