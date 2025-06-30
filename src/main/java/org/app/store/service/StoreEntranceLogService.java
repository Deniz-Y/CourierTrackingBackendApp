package org.app.store.service;

import lombok.RequiredArgsConstructor;
import org.app.courier.entity.Courier;
import org.app.store.dto.StoreEntranceLogDto;
import org.app.store.entity.Store;
import org.app.store.entity.StoreEntranceLog;
import org.app.store.mapper.StoreEntranceLogMapper;
import org.app.store.repository.StoreEntranceLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * Service for managing Store Entrance Logs.
 * Provides methods for recording entries and checking recent entry times
 * to avoid duplicate logs within 1-minute threshold.
 */
@Service
@RequiredArgsConstructor
public class StoreEntranceLogService {

    private final StoreEntranceLogRepository repository;
    private final StoreEntranceLogMapper mapper;


    public StoreEntranceLog logEntrance(Courier courier, Store store, LocalDateTime entryTime) {
        return repository.save(StoreEntranceLog.builder()
                .courier(courier)
                .store(store)
                .entranceTime(entryTime)
                .build());
    }

    /**
     * Retrieves the most recent entrance log of a courier into a store.
     */
    public Optional<StoreEntranceLog> getLastEntrance(Long courierId, Long storeId) {
        return repository.findTopByCourierIdAndStoreIdOrderByEntranceTimeDesc(courierId, storeId);
    }

    /**
     * Determines whether a new log should be recorded:
     * Returns true if no prior record exists, or if last log is older than 1 minute.
     */
    public boolean canLogEntrance(Courier courier, Store store, LocalDateTime now) {
        return getLastEntrance(courier.getId(), store.getId())
                .map(entry -> entry.getEntranceTime().plusMinutes(1).isBefore(now))
                .orElse(true);
    }


    public List<StoreEntranceLogDto> getAllLogs() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }


    public List<StoreEntranceLogDto> getLogsByCourier(Long courierId) {
        return repository.findByCourierId(courierId).stream()
                .map(mapper::toDto)
                .toList();
    }


    public List<StoreEntranceLogDto> getLogsByStore(Long storeId) {
        return repository.findByStoreId(storeId).stream()
                .map(mapper::toDto)
                .toList();
    }
}
