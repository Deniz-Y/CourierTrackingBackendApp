package org.app.courier.service;

import lombok.RequiredArgsConstructor;
import org.app.courier.dto.CourierLocationDto;
import org.app.courier.entity.Courier;
import org.app.courier.entity.CourierLocation;
import org.app.courier.mapper.CourierLocationMapper;
import org.app.courier.repository.CourierLocationRepository;
import org.app.common.service.DistanceService;
import org.app.courier.repository.CourierRepository;
import org.app.store.service.StoreCheckService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourierLocationService {

    private final CourierLocationRepository courierLocationRepository;
    private final DistanceService distanceService;
    private final StoreCheckService storeCheckService;
    private final CourierRepository courierRepository;
    private final CourierLocationMapper courierLocationMapper;


    public CourierLocationDto addCourierLocation(CourierLocationDto request) {
        Courier courier = courierRepository.findById(request.getCourierId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Courier Id"));

        CourierLocation entity = courierLocationMapper.toEntity(request, courier);

        CourierLocation saved = courierLocationRepository.save(entity);

        //In the future, we can consider processing store check logic asynchronously to improve performance and avoid blocking.
        storeCheckService.handleStoreCheck(saved);

        return courierLocationMapper.toDto(saved);
    }

    /**
     * Returns the total travel distance for the specified courier.
     *
     * Note: In the future, we can consider caching or periodically storing computed distance values in the database,
     * possibly using a scheduled batch job.
     */
    public double getTotalDistance(Long courierId) {
        if (!courierRepository.existsById(courierId)) {
            throw new IllegalArgumentException("Courier not found with ID: " + courierId);
        }
        return distanceService.getTotalTravelDistance(courierId);
    }

}
