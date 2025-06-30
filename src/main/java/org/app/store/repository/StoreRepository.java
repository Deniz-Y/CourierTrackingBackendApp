package org.app.store.repository;

import org.app.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    /**
     * Checks if a store exists by exact latitude and longitude.
     * Used to prevent duplicate store locations.
     */
    boolean existsByLatAndLng(double lat, double lng);
}

