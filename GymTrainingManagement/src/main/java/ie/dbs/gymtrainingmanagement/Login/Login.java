
package ie.dbs.gymtrainingmanagement.Login;

import jakarta.persistence.*;

@Entity
@Table(name = "logins")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role_id", nullable = false)
    private int roleId;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    // Construtor obrigatório para o JPA
    public Login() {
    }

    // Construtor personalizado
    public Login(String userName, String password,
                 int roleId, boolean isActive) {
        this.userName = userName;
        this.password = password;
        this.roleId = roleId;
        this.isActive = isActive;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
