package ie.dbs.gymtrainingmanagement.Member;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class MemberForm {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100,
            message = "Full name must contain between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    @Size(max = 150, message = "Email can have at most 150 characters")
    private String email;

    @Pattern(regexp = "^$|^[0-9+()\\s-]{7,20}$",
            message = "Phone must have 7 to 20 characters (digits, spaces, +, - and brackets)")
    private String phone;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @NotNull(message = "A gym must be selected")
    private Long gymId;

    // Optional
    private Long loginId;

    private boolean active = true;

    public MemberForm() {
    }

    public MemberForm(String fullName, String email, String phone, LocalDate birthDate,
                      Long gymId, Long loginId, boolean active) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gymId = gymId;
        this.loginId = loginId;
        this.active = active;
    }
}
