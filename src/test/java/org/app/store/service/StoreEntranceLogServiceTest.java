package org.app.store.service;

import org.app.courier.entity.Courier;
import org.app.store.dto.StoreEntranceLogDto;
import org.app.store.entity.Store;
import org.app.store.entity.StoreEntranceLog;
import org.app.store.mapper.StoreEntranceLogMapper;
import org.app.store.repository.StoreEntranceLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StoreEntranceLogServiceTest {

    @Mock
    private StoreEntranceLogRepository repository;

    @Mock
    private StoreEntranceLogMapper mapper;

    @InjectMocks
    private StoreEntranceLogService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void logEntrance_shouldSaveAndReturnEntity() {
        Courier courier = new Courier();
        courier.setId(1L);

        Store store = new Store();
        store.setId(2L);

        LocalDateTime now = LocalDateTime.now();

        StoreEntranceLog logToSave = StoreEntranceLog.builder()
                .courier(courier)
                .store(store)
                .entranceTime(now)
                .build();

        StoreEntranceLog savedLog = StoreEntranceLog.builder()
                .id(100L)
                .courier(courier)
                .store(store)
                .entranceTime(now)
                .build();

        when(repository.save(any(StoreEntranceLog.class))).thenReturn(savedLog);

        StoreEntranceLog result = service.logEntrance(courier, store, now);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        verify(repository).save(any(StoreEntranceLog.class));
    }

    @Test
    void getLastEntrance_shouldReturnOptionalLog() {
        Long courierId = 1L;
        Long storeId = 2L;

        StoreEntranceLog log = StoreEntranceLog.builder()
                .id(10L)
                .build();

        when(repository.findTopByCourierIdAndStoreIdOrderByEntranceTimeDesc(courierId, storeId))
                .thenReturn(Optional.of(log));

        Optional<StoreEntranceLog> result = service.getLastEntrance(courierId, storeId);

        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getId());
        verify(repository).findTopByCourierIdAndStoreIdOrderByEntranceTimeDesc(courierId, storeId);
    }

    @Test
    void canLogEntrance_shouldReturnTrueIfNoPreviousLog() {
        Courier courier = new Courier();
        courier.setId(1L);

        Store store = new Store();
        store.setId(2L);

        LocalDateTime now = LocalDateTime.now();

        when(repository.findTopByCourierIdAndStoreIdOrderByEntranceTimeDesc(1L, 2L))
                .thenReturn(Optional.empty());

        assertTrue(service.canLogEntrance(courier, store, now));
    }

    @Test
    void canLogEntrance_shouldReturnTrueIfLastLogOlderThanOneMinute() {
        Courier courier = new Courier();
        courier.setId(1L);

        Store store = new Store();
        store.setId(2L);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oldTime = now.minusMinutes(2);

        StoreEntranceLog lastLog = StoreEntranceLog.builder()
                .entranceTime(oldTime)
                .build();

        when(repository.findTopByCourierIdAndStoreIdOrderByEntranceTimeDesc(1L, 2L))
                .thenReturn(Optional.of(lastLog));

        assertTrue(service.canLogEntrance(courier, store, now));
    }

    @Test
    void canLogEntrance_shouldReturnFalseIfLastLogWithinOneMinute() {
        Courier courier = new Courier();
        courier.setId(1L);

        Store store = new Store();
        store.setId(2L);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime recentTime = now.minusSeconds(30);

        StoreEntranceLog lastLog = StoreEntranceLog.builder()
                .entranceTime(recentTime)
                .build();

        when(repository.findTopByCourierIdAndStoreIdOrderByEntranceTimeDesc(1L, 2L))
                .thenReturn(Optional.of(lastLog));

        assertFalse(service.canLogEntrance(courier, store, now));
    }

    @Test
    void getAllLogs_shouldReturnDtoList() {
        StoreEntranceLog log1 = StoreEntranceLog.builder().id(1L).build();
        StoreEntranceLog log2 = StoreEntranceLog.builder().id(2L).build();

        StoreEntranceLogDto dto1 = new StoreEntranceLogDto();
        StoreEntranceLogDto dto2 = new StoreEntranceLogDto();

        when(repository.findAll()).thenReturn(List.of(log1, log2));
        when(mapper.toDto(log1)).thenReturn(dto1);
        when(mapper.toDto(log2)).thenReturn(dto2);

        List<StoreEntranceLogDto> result = service.getAllLogs();

        assertEquals(2, result.size());
        verify(repository).findAll();
        verify(mapper, times(2)).toDto(any(StoreEntranceLog.class));
    }

    @Test
    void getLogsByCourier_shouldReturnDtoList() {
        Long courierId = 1L;
        StoreEntranceLog log = StoreEntranceLog.builder().id(5L).build();
        StoreEntranceLogDto dto = new StoreEntranceLogDto();

        when(repository.findByCourierId(courierId)).thenReturn(List.of(log));
        when(mapper.toDto(log)).thenReturn(dto);

        List<StoreEntranceLogDto> result = service.getLogsByCourier(courierId);

        assertEquals(1, result.size());
        verify(repository).findByCourierId(courierId);
        verify(mapper).toDto(log);
    }

    @Test
    void getLogsByStore_shouldReturnDtoList() {
        Long storeId = 2L;
        StoreEntranceLog log = StoreEntranceLog.builder().id(7L).build();
        StoreEntranceLogDto dto = new StoreEntranceLogDto();

        when(repository.findByStoreId(storeId)).thenReturn(List.of(log));
        when(mapper.toDto(log)).thenReturn(dto);

        List<StoreEntranceLogDto> result = service.getLogsByStore(storeId);

        assertEquals(1, result.size());
        verify(repository).findByStoreId(storeId);
        verify(mapper).toDto(log);
    }
}
