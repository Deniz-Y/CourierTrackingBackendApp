package org.app.courier.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "courier_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourierLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //primary key

    @ManyToOne
    @JoinColumn(name = "courier_id", nullable = false) //foreign key. many to one relationship between courier_locations and courier.
    private Courier courier;

    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;
    @Column(nullable = false)
    private LocalDateTime timestamp;
}

