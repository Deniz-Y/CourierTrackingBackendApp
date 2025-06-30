package org.app.store.service;

import org.app.common.service.HaversineDistanceCalculator;
import org.app.courier.entity.Courier;
import org.app.courier.entity.CourierLocation;
import org.app.store.entity.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class StoreCheckServiceTest {

    @Mock
    private StoreService storeService;

    @Mock
    private StoreEntranceLogService storeEntranceLogService;

    @Mock
    private HaversineDistanceCalculator distanceCalculator;

    @InjectMocks
    private StoreCheckService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleStoreCheck_logsEntranceIfWithinDistanceAndAllowed() {
        Courier courier = new Courier();
        courier.setId(1L);

        CourierLocation location = CourierLocation.builder()
                .courier(courier)
                .latitude(40.0)
                .longitude(29.0)
                .timestamp(LocalDateTime.now())
                .build();

        Store store1 = Store.builder()
                .id(1L)
                .lat(40.0005)
                .lng(29.0005)
                .build();

        Store store2 = Store.builder()
                .id(2L)
                .lat(41.0)
                .lng(30.0)
                .build();

        // Mock storeService to return two stores
        when(storeService.getAllStoreEntities()).thenReturn(List.of(store1, store2));

        // Mock distances: store1 is within threshold, store2 is outside
        when(distanceCalculator.calculateDistance(
                location.getLatitude(), location.getLongitude(),
                store1.getLat(), store1.getLng()))
                .thenReturn(50.0);  // within threshold

        when(distanceCalculator.calculateDistance(
                location.getLatitude(), location.getLongitude(),
                store2.getLat(), store2.getLng()))
                .thenReturn(150.0);  // outside threshold

        // Allow logging only for store1
        when(storeEntranceLogService.canLogEntrance(courier, store1, location.getTimestamp()))
                .thenReturn(true);

        // For store2, logging shouldn't be checked, but let's be safe:
        when(storeEntranceLogService.canLogEntrance(courier, store2, location.getTimestamp()))
                .thenReturn(false);

        // Mock logEntrance to just return a dummy entity
        when(storeEntranceLogService.logEntrance(courier, store1, location.getTimestamp()))
                .thenReturn(null);

        // Run method
        service.handleStoreCheck(location);

        // Verify interactions
        verify(storeService).getAllStoreEntities();

        verify(distanceCalculator).calculateDistance(
                location.getLatitude(), location.getLongitude(),
                store1.getLat(), store1.getLng());

        verify(distanceCalculator).calculateDistance(
                location.getLatitude(), location.getLongitude(),
                store2.getLat(), store2.getLng());

        verify(storeEntranceLogService).canLogEntrance(courier, store1, location.getTimestamp());
        verify(storeEntranceLogService, never()).logEntrance(courier, store2, location.getTimestamp());

        verify(storeEntranceLogService).logEntrance(courier, store1, location.getTimestamp());
    }

    @Test
    void handleStoreCheck_doesNotLogIfOutsideDistance() {
        Courier courier = new Courier();
        courier.setId(1L);

        CourierLocation location = CourierLocation.builder()
                .courier(courier)
                .latitude(40.0)
                .longitude(29.0)
                .timestamp(LocalDateTime.now())
                .build();

        Store store = Store.builder()
                .id(1L)
                .lat(41.0)
                .lng(30.0)
                .build();

        when(storeService.getAllStoreEntities()).thenReturn(List.of(store));

        when(distanceCalculator.calculateDistance(
                location.getLatitude(), location.getLongitude(),
                store.getLat(), store.getLng()))
                .thenReturn(150.0); // outside threshold

        service.handleStoreCheck(location);

        verify(storeEntranceLogService, never()).canLogEntrance(any(), any(), any());
        verify(storeEntranceLogService, never()).logEntrance(any(), any(), any());
    }
}
