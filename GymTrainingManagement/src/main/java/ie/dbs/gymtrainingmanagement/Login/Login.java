
package ie.dbs.gymtrainingmanagement.Login;

import ie.dbs.gymtrainingmanagement.Role.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "logins")
@Getter
@Setter
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(optional = false)
    @JoinColumn(name = "roleId", nullable = false)
    private Role role;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public Login() {
    }

    public Login(String userName, String password, Role role, boolean isActive) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
    }
}
