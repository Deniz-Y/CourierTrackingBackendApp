package org.app.store.entity;

import jakarta.persistence.*;
import lombok.*;
import org.app.courier.entity.Courier;

import java.time.LocalDateTime;

@Entity
@Table(name = "store_entrance_logs",
        indexes = {
                @Index(name = "idx_courier_id", columnList = "courier_id"),
                @Index(name = "idx_store_id", columnList = "store_id"),
                @Index(name = "idx_entrance_time", columnList = "entrance_time")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreEntranceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", nullable = false)
    private Courier courier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "entrance_time", nullable = false)
    private LocalDateTime entranceTime;

    /*
     * Indexes on courier_id, store_id, and entrance_time columns improve query performance.
     * These indexes speed up searches and filtering by courier, store, or time,
     * which are common in reporting entrance logs.
     * Without indexes, queries on large data sets would require full table scans,
     * leading to slow response times.
     */
}
