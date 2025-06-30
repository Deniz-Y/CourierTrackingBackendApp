package org.app.common.service;

import lombok.RequiredArgsConstructor;
import org.app.courier.entity.CourierLocation;
import org.app.courier.repository.CourierLocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistanceService {

    private final CourierLocationRepository courierLocationRepository;
    private final HaversineDistanceCalculator distanceCalculator;

    /**
     * Retrieves courier locations from the database
     * and calculates the total distance traveled in chronological order.
     */

    public double getTotalTravelDistance(Long courierId) {
        List<CourierLocation> locations = courierLocationRepository.findByCourierIdOrderByTimestampAsc(courierId);
        double totalDistance = 0.0;

        for (int i = 1; i < locations.size(); i++) {
            CourierLocation prev = locations.get(i - 1);
            CourierLocation curr = locations.get(i);

            totalDistance += distanceCalculator.calculateDistance(
                    prev.getLatitude(),
                    prev.getLongitude(),
                    curr.getLatitude(),
                    curr.getLongitude()
            );
        }

        return totalDistance;
    }
}
