package ie.dbs.gymtrainingmanagement.Member;

import ie.dbs.gymtrainingmanagement.Login.Login;
import ie.dbs.gymtrainingmanagement.Gym.Gym;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "members")
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDate registrationDate;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    // Optional: a member only needs a login if they will sign in to the system.
    @OneToOne
    @JoinColumn(name = "login_id", unique = true)
    private Login login;

    public Member() {
    }

    public Member(String fullName, String email, String phone, LocalDate birthDate,
                  boolean active, Gym gym, Login login) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.active = active;
        this.gym = gym;
        this.login = login;
        this.registrationDate = LocalDate.now();
    }
}
