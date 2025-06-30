package org.app.common.service;

import org.app.courier.entity.CourierLocation;
import org.app.courier.repository.CourierLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DistanceServiceTest {

    private CourierLocationRepository courierLocationRepository;
    private HaversineDistanceCalculator distanceCalculator;
    private DistanceService distanceService;

    @BeforeEach
    void setUp() {
        courierLocationRepository = mock(CourierLocationRepository.class);
        distanceCalculator = mock(HaversineDistanceCalculator.class);
        distanceService = new DistanceService(courierLocationRepository, distanceCalculator);
    }

    @Test
    void testGetTotalTravelDistance_WithMultipleLocations() {
        Long courierId = 1L;

        CourierLocation loc1 = new CourierLocation();
        loc1.setLatitude(40.0);
        loc1.setLongitude(29.0);

        CourierLocation loc2 = new CourierLocation();
        loc2.setLatitude(41.0);
        loc2.setLongitude(29.5);

        CourierLocation loc3 = new CourierLocation();
        loc3.setLatitude(42.0);
        loc3.setLongitude(30.0);

        List<CourierLocation> locations = Arrays.asList(loc1, loc2, loc3);

        when(courierLocationRepository.findByCourierIdOrderByTimestampAsc(courierId)).thenReturn(locations);
        when(distanceCalculator.calculateDistance(40.0, 29.0, 41.0, 29.5)).thenReturn(120000.0); // örnek 120 km
        when(distanceCalculator.calculateDistance(41.0, 29.5, 42.0, 30.0)).thenReturn(110000.0); // örnek 110 km

        double totalDistance = distanceService.getTotalTravelDistance(courierId);

        assertEquals(230000.0, totalDistance);
        verify(courierLocationRepository, times(1)).findByCourierIdOrderByTimestampAsc(courierId);
        verify(distanceCalculator, times(2)).calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    void testGetTotalTravelDistance_WithLessThanTwoLocations() {
        Long courierId = 2L;
        List<CourierLocation> locations = List.of(new CourierLocation());

        when(courierLocationRepository.findByCourierIdOrderByTimestampAsc(courierId)).thenReturn(locations);

        double totalDistance = distanceService.getTotalTravelDistance(courierId);

        assertEquals(0.0, totalDistance);
        verify(distanceCalculator, never()).calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }
}
