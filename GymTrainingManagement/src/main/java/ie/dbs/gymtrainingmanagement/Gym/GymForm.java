package ie.dbs.gymtrainingmanagement.Gym;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GymForm {

    @NotBlank(message = "Gym name is required")
    @Size(min = 2, max = 100,
            message = "Gym name must contain between 2 and 100 characters")
    private String name;

    @Pattern(regexp = "^$|^[0-9+()\\s-]{7,20}$",
            message = "Phone must have 7 to 20 characters (digits, spaces, +, - and brackets)")
    private String phone;

    @NotBlank(message = "Street is required")
    @Size(max = 150, message = "Street can have at most 150 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City can have at most 100 characters")
    private String city;

    @NotBlank(message = "County is required")
    @Size(max = 100, message = "County can have at most 100 characters")
    private String county;

    @Pattern(regexp = "^$|^[A-Za-z0-9]{3}\\s?[A-Za-z0-9]{4}$",
            message = "Eircode must have 7 characters, for example D02 X285")
    private String eircode;

    public GymForm() {
    }

    public GymForm(String name, String phone, String street,
                   String city, String county, String eircode) {
        this.name = name;
        this.phone = phone;
        this.street = street;
        this.city = city;
        this.county = county;
        this.eircode = eircode;
    }
}
