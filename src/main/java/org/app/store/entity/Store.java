package org.app.store.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Store entity representing a Migros store location.
 * Each store has a name and a unique geographical location (latitude & longitude).
 */
@Entity
@Table(
        name = "stores",
        uniqueConstraints = @UniqueConstraint(columnNames = {"lat", "lng"}) // Prevents duplicate store locations
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;
}
