package ie.dbs.gymtrainingmanagement.WorkoutPlan;

import ie.dbs.gymtrainingmanagement.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "workout_plans")
@Getter
@Setter
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public WorkoutPlan() {
    }

    public WorkoutPlan(String name, String notes, LocalDate startDate, boolean active, Member member) {
        this.name = name;
        this.notes = notes;
        this.startDate = startDate;
        this.active = active;
        this.member = member;
    }
}
