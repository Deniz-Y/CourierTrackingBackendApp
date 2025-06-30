package org.app.courier.service;

import org.app.courier.dto.CourierLocationDto;
import org.app.courier.entity.Courier;
import org.app.courier.entity.CourierLocation;
import org.app.courier.mapper.CourierLocationMapper;
import org.app.courier.repository.CourierLocationRepository;
import org.app.courier.repository.CourierRepository;
import org.app.common.service.DistanceService;
import org.app.store.service.StoreCheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourierLocationServiceTest {

    @Mock
    private CourierLocationRepository courierLocationRepository;

    @Mock
    private DistanceService distanceService;

    @Mock
    private StoreCheckService storeCheckService;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierLocationMapper courierLocationMapper;

    @InjectMocks
    private CourierLocationService courierLocationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCourierLocation_success() {
        CourierLocationDto requestDto = new CourierLocationDto();
        requestDto.setCourierId(1L);
        // diğer alanları da set edilebilir

        Courier courier = new Courier();
        courier.setId(1L);

        CourierLocation entity = new CourierLocation();
        CourierLocation savedEntity = new CourierLocation();

        CourierLocationDto savedDto = new CourierLocationDto();

        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        when(courierLocationMapper.toEntity(requestDto, courier)).thenReturn(entity);
        when(courierLocationRepository.save(entity)).thenReturn(savedEntity);
        when(courierLocationMapper.toDto(savedEntity)).thenReturn(savedDto);

        CourierLocationDto result = courierLocationService.addCourierLocation(requestDto);

        assertNotNull(result);
        verify(storeCheckService).handleStoreCheck(savedEntity);
        verify(courierRepository).findById(1L);
        verify(courierLocationRepository).save(entity);
        verify(courierLocationMapper).toDto(savedEntity);
    }

    @Test
    void addCourierLocation_invalidCourierId_throws() {
        CourierLocationDto requestDto = new CourierLocationDto();
        requestDto.setCourierId(99L);

        when(courierRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            courierLocationService.addCourierLocation(requestDto);
        });

        assertEquals("Invalid Courier Id", ex.getMessage());
        verify(courierRepository).findById(99L);
        verifyNoMoreInteractions(courierLocationRepository, storeCheckService, courierLocationMapper);
    }

    @Test
    void getTotalDistance_success() {
        Long courierId = 1L;

        when(courierRepository.existsById(courierId)).thenReturn(true);
        when(distanceService.getTotalTravelDistance(courierId)).thenReturn(123.45);

        double distance = courierLocationService.getTotalDistance(courierId);

        assertEquals(123.45, distance);
        verify(courierRepository).existsById(courierId);
        verify(distanceService).getTotalTravelDistance(courierId);
    }

    @Test
    void getTotalDistance_courierNotFound_throws() {
        Long courierId = 99L;

        when(courierRepository.existsById(courierId)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            courierLocationService.getTotalDistance(courierId);
        });

        assertEquals("Courier not found with ID: " + courierId, ex.getMessage());
        verify(courierRepository).existsById(courierId);
        verifyNoMoreInteractions(distanceService);
    }
}
