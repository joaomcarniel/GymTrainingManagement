package ie.dbs.gymtrainingmanagement.Gym;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
@Getter
@Setter
public class Address {

    @Column(name = "street", nullable = false, length = 150)
    private String street;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "county", nullable = false, length = 100)
    private String county;

    @Column(name = "eircode", length = 10)
    private String eircode;

    public Address() {
    }

    public Address(String street, String city, String county, String eircode) {
        this.street = street;
        this.city = city;
        this.county = county;
        this.eircode = eircode;
    }

    public String getFullAddress() {
        return Stream.of(street, city, county, eircode)
                .filter(part -> part != null && !part.isBlank())
                .collect(Collectors.joining(", "));
    }
}
