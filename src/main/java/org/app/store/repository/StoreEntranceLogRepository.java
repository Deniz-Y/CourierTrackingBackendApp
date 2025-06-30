package org.app.store.repository;

import org.app.store.entity.StoreEntranceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreEntranceLogRepository extends JpaRepository<StoreEntranceLog, Long> {

    Optional<StoreEntranceLog> findTopByCourierIdAndStoreIdOrderByEntranceTimeDesc(Long courierId, Long storeId);

    List<StoreEntranceLog> findByCourierId(Long courierId);

    List<StoreEntranceLog> findByStoreId(Long storeId);
}
