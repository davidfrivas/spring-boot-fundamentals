package com.davidfrivas.store.entities;

import jakarta.persistence.*;
import lombok.*;

@Setter // Lombok setter
@Getter // Lombok getter
@Entity // JPA Entity
@Table(name = "tags") // Name of DB table
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
}
