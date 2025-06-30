package org.app.courier.service;

import org.app.courier.dto.CourierDto;
import org.app.courier.entity.Courier;
import org.app.courier.mapper.CourierMapper;
import org.app.courier.repository.CourierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourierServiceTest {

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierMapper courierMapper;

    @InjectMocks
    private CourierService courierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCourier_success() {
        CourierDto dto = new CourierDto(null, "John Doe", "C123");
        Courier entity = Courier.builder().id(null).name("John Doe").courierNumber("C123").build();
        Courier savedEntity = Courier.builder().id(1L).name("John Doe").courierNumber("C123").build();
        CourierDto savedDto = new CourierDto(1L, "John Doe", "C123");

        when(courierRepository.existsByCourierNumber("C123")).thenReturn(false);
        when(courierMapper.toEntity(dto)).thenReturn(entity);
        when(courierRepository.save(entity)).thenReturn(savedEntity);
        when(courierMapper.toDto(savedEntity)).thenReturn(savedDto);

        CourierDto result = courierService.createCourier(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("C123", result.getCourierNumber());

        verify(courierRepository).existsByCourierNumber("C123");
        verify(courierRepository).save(entity);
    }

    @Test
    void createCourier_duplicateCourierNumber_throws() {
        CourierDto dto = new CourierDto(null, "John Doe", "C123");
        when(courierRepository.existsByCourierNumber("C123")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            courierService.createCourier(dto);
        });
        assertEquals("Courier number already exists.", ex.getMessage());
        verify(courierRepository).existsByCourierNumber("C123");
        verify(courierRepository, never()).save(any());
    }

    @Test
    void getCourierById_found() {
        Courier entity = Courier.builder().id(1L).name("John").courierNumber("C1").build();
        CourierDto dto = new CourierDto(1L, "John", "C1");

        when(courierRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(courierMapper.toDto(entity)).thenReturn(dto);

        Optional<CourierDto> result = courierService.getCourierById(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getName());
    }

    @Test
    void getCourierById_notFound() {
        when(courierRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CourierDto> result = courierService.getCourierById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void getAllCouriers() {
        Courier entity1 = Courier.builder().id(1L).name("John").courierNumber("C1").build();
        Courier entity2 = Courier.builder().id(2L).name("Jane").courierNumber("C2").build();
        CourierDto dto1 = new CourierDto(1L, "John", "C1");
        CourierDto dto2 = new CourierDto(2L, "Jane", "C2");

        when(courierRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(courierMapper.toDto(entity1)).thenReturn(dto1);
        when(courierMapper.toDto(entity2)).thenReturn(dto2);

        List<CourierDto> result = courierService.getAllCouriers();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("Jane", result.get(1).getName());
    }

    @Test
    void deleteCourier_existing() {
        when(courierRepository.existsById(1L)).thenReturn(true);
        doNothing().when(courierRepository).deleteById(1L);

        assertDoesNotThrow(() -> courierService.deleteCourier(1L));
        verify(courierRepository).deleteById(1L);
    }

    @Test
    void deleteCourier_notExisting() {
        when(courierRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            courierService.deleteCourier(1L);
        });
        assertEquals("Courier not found with ID: 1", ex.getMessage());
        verify(courierRepository, never()).deleteById(any());
    }

    @Test
    void updateCourier_success() {
        CourierDto dto = new CourierDto(null, "Updated Name", "C123");
        Courier existing = Courier.builder().id(1L).name("Old Name").courierNumber("C123").build();
        Courier updatedEntity = Courier.builder().id(1L).name("Updated Name").courierNumber("C123").build();
        CourierDto updatedDto = new CourierDto(1L, "Updated Name", "C123");

        when(courierRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courierRepository.existsByCourierNumber("C123")).thenReturn(false);
        when(courierRepository.save(existing)).thenReturn(updatedEntity);
        when(courierMapper.toDto(updatedEntity)).thenReturn(updatedDto);

        CourierDto result = courierService.updateCourier(1L, dto);

        assertEquals("Updated Name", result.getName());
        verify(courierRepository).findById(1L);
        verify(courierRepository).save(existing);
    }

    @Test
    void updateCourier_duplicateCourierNumber_throws() {
        CourierDto dto = new CourierDto(null, "New Name", "C999");
        Courier existing = Courier.builder().id(1L).name("Old Name").courierNumber("C123").build();

        when(courierRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courierRepository.existsByCourierNumber("C999")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            courierService.updateCourier(1L, dto);
        });
        assertEquals("Courier number already exists.", ex.getMessage());
    }

    @Test
    void updateCourier_notFound() {
        CourierDto dto = new CourierDto(null, "Name", "C123");
        when(courierRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            courierService.updateCourier(1L, dto);
        });
        assertEquals("Courier not found with ID: 1", ex.getMessage());
    }
}
