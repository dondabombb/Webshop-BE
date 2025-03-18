package nl.hsleiden.WebshopBE.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
public class AddressModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String houseNumber;

    private String houseNumberAddition;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    public AddressModel(String street, String houseNumber, String houseNumberAddition, 
                        String postalCode, String city, String country) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.houseNumberAddition = houseNumberAddition;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }
} 