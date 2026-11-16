package ie.dbs.gymtrainingmanagement.Login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    @NotBlank(message = "User name is required")
    @Size(min = 3, max = 100,
            message = "User name must contain between 3 and 100 characters")
    private String userName;

    @NotBlank(message = "Password is required")
    @Size(min = 3, max = 100,
            message = "Password must contain between 3 and 100 characters")
    private String password;

    @NotNull(message = "A role must be selected")
    private Integer roleId;

    private boolean active;

    public LoginForm() {
    }

    public LoginForm(String userName, String password, Integer roleId, boolean isActive) {
        this.userName = userName;
        this.password = password;
        this.roleId = roleId;
        this.active = isActive;
    }
}
