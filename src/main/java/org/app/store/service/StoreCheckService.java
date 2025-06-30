package org.app.store.service;

import lombok.RequiredArgsConstructor;
import org.app.common.service.HaversineDistanceCalculator;
import org.app.courier.entity.Courier;
import org.app.courier.entity.CourierLocation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StoreCheckService {

    private static final double DISTANCE_THRESHOLD_METERS = 100.0;

    private final StoreService storeService;
    private final StoreEntranceLogService storeEntranceLogService;
    private final HaversineDistanceCalculator distanceCalculator;

    /**
     * Checks whether the courier is within 100 meters of any store.
     * If so, and at least 1 minute has passed since last log, it logs a new store entrance.
     */
    public void handleStoreCheck(CourierLocation location) {
        Courier courier = location.getCourier();
        LocalDateTime now = location.getTimestamp();

        storeService.getAllStoreEntities().forEach(store -> {
            double distanceToStore = distanceCalculator.calculateDistance(
                    location.getLatitude(),
                    location.getLongitude(),
                    store.getLat(),
                    store.getLng()
            );

            if (distanceToStore <= DISTANCE_THRESHOLD_METERS) {
                if (storeEntranceLogService.canLogEntrance(courier, store, now)) {
                    storeEntranceLogService.logEntrance(courier, store, now);
                }
            }
        });
    }
}
