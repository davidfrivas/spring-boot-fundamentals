package com.davidfrivas.mouse_colony_app.entities;

import jakarta.persistence.*;
import lombok.*;

@Setter // Lombok setter
@Getter // Lombok getter
@Entity // JPA Entity
@Table(name = "addresses") // Name of DB table
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "zip")
    private String zip;

    @Column(name = "state")
    private String state;
}
