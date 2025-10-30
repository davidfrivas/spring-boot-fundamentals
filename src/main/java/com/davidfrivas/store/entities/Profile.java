package com.davidfrivas.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter // Lombok setter
@Getter // Lombok getter
@Entity // JPA Entity
@Table(name = "profiles") // Name of DB table
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "bio")
    private String bio;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "loyalty_points")
    private Integer loyaltyPoints;
}
