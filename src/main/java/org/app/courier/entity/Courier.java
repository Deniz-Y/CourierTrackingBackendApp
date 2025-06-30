package org.app.courier.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "couriers", uniqueConstraints = {@UniqueConstraint(columnNames = "courier_number")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // I assume that each courier has unique courier_number
    @Column(name = "courier_number", nullable = false, unique = true, length = 50)
    private String courierNumber;
}

