package com.anae.api.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class Address {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String adress;

    private String adress2;

    private String region;

    @Column(name = "postal_code", nullable = false)
    private Integer postalCode;

    private Integer phonenumber;

    @Column(nullable = false)
    private String country;

    @OneToOne(mappedBy = "adress")
    private UserData linkedUser;

    public Address(
            String city,
            String address,
            String address2,
            String region,
            Integer postalCode,
            Integer phonenumber,
            String country) {
        this.city = city;
        this.adress = address;
        this.adress2 = address2;
        this.region = region;
        this.postalCode = postalCode;
        this.phonenumber = phonenumber;
        this.country = country;
    }
}