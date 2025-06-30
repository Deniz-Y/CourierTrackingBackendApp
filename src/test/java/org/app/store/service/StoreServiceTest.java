package org.app.store.service;

import org.app.store.dto.StoreDto;
import org.app.store.entity.Store;
import org.app.store.mapper.StoreMapper;
import org.app.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreMapper storeMapper;

    @InjectMocks
    private StoreService storeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllStores_returnsDtoList() {
        Store store1 = new Store(1L, "Store A", 10.0, 20.0);
        Store store2 = new Store(2L, "Store B", 15.0, 25.0);
        List<Store> stores = List.of(store1, store2);

        StoreDto dto1 = new StoreDto(1L, "Store A", 10.0, 20.0);
        StoreDto dto2 = new StoreDto(2L, "Store B", 15.0, 25.0);

        when(storeRepository.findAll()).thenReturn(stores);
        when(storeMapper.toDto(store1)).thenReturn(dto1);
        when(storeMapper.toDto(store2)).thenReturn(dto2);

        List<StoreDto> result = storeService.getAllStores();

        assertEquals(2, result.size());
        assertEquals("Store A", result.get(0).getName());
        assertEquals("Store B", result.get(1).getName());

        verify(storeRepository).findAll();
        verify(storeMapper, times(2)).toDto(any());
    }

    @Test
    void getStoreById_found() {
        Store store = new Store(1L, "Store A", 10.0, 20.0);
        StoreDto dto = new StoreDto(1L, "Store A", 10.0, 20.0);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(storeMapper.toDto(store)).thenReturn(dto);

        Optional<StoreDto> result = storeService.getStoreById(1L);

        assertTrue(result.isPresent());
        assertEquals("Store A", result.get().getName());

        verify(storeRepository).findById(1L);
        verify(storeMapper).toDto(store);
    }

    @Test
    void getStoreById_notFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<StoreDto> result = storeService.getStoreById(1L);

        assertFalse(result.isPresent());

        verify(storeRepository).findById(1L);
        verifyNoInteractions(storeMapper);
    }

    @Test
    void createStore_success() {
        StoreDto dto = new StoreDto(null, "Store A", 10.0, 20.0);
        Store entity = new Store(null, "Store A", 10.0, 20.0);
        Store savedEntity = new Store(1L, "Store A", 10.0, 20.0);
        StoreDto savedDto = new StoreDto(1L, "Store A", 10.0, 20.0);

        when(storeRepository.existsByLatAndLng(10.0, 20.0)).thenReturn(false);
        when(storeMapper.toEntity(dto)).thenReturn(entity);
        when(storeRepository.save(entity)).thenReturn(savedEntity);
        when(storeMapper.toDto(savedEntity)).thenReturn(savedDto);

        StoreDto result = storeService.createStore(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Store A", result.getName());

        verify(storeRepository).existsByLatAndLng(10.0, 20.0);
        verify(storeRepository).save(entity);
        verify(storeMapper).toDto(savedEntity);
    }

    @Test
    void createStore_duplicateLocation_throws() {
        StoreDto dto = new StoreDto(null, "Store A", 10.0, 20.0);

        when(storeRepository.existsByLatAndLng(10.0, 20.0)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            storeService.createStore(dto);
        });

        assertEquals("Store at this location already exists.", ex.getMessage());

        verify(storeRepository).existsByLatAndLng(10.0, 20.0);
        verify(storeRepository, never()).save(any());
        verifyNoInteractions(storeMapper);
    }

    @Test
    void updateStore_success() {
        StoreDto dto = new StoreDto(null, "Updated Store", 11.0, 21.0);
        Store existing = new Store(1L, "Store A", 10.0, 20.0);
        Store updatedEntity = new Store(1L, "Updated Store", 11.0, 21.0);
        StoreDto updatedDto = new StoreDto(1L, "Updated Store", 11.0, 21.0);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(storeRepository.existsByLatAndLng(11.0, 21.0)).thenReturn(false);
        when(storeRepository.save(existing)).thenReturn(updatedEntity);
        when(storeMapper.toDto(updatedEntity)).thenReturn(updatedDto);

        StoreDto result = storeService.updateStore(1L, dto);

        assertEquals("Updated Store", result.getName());
        assertEquals(11.0, result.getLat());
        assertEquals(21.0, result.getLng());

        verify(storeRepository).findById(1L);
        verify(storeRepository).existsByLatAndLng(11.0, 21.0);
        verify(storeRepository).save(existing);
        verify(storeMapper).toDto(updatedEntity);
    }

    @Test
    void updateStore_notFound_throws() {
        StoreDto dto = new StoreDto(null, "Updated Store", 11.0, 21.0);

        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            storeService.updateStore(1L, dto);
        });

        assertEquals("Store not found with id: 1", ex.getMessage());

        verify(storeRepository).findById(1L);
        verify(storeRepository, never()).existsByLatAndLng(anyDouble(), anyDouble());
        verify(storeRepository, never()).save(any());
        verifyNoInteractions(storeMapper);
    }

    @Test
    void updateStore_duplicateLocation_throws() {
        StoreDto dto = new StoreDto(null, "Updated Store", 11.0, 21.0);
        Store existing = new Store(1L, "Store A", 10.0, 20.0);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(storeRepository.existsByLatAndLng(11.0, 21.0)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            storeService.updateStore(1L, dto);
        });

        assertEquals("Another store at this location already exists.", ex.getMessage());

        verify(storeRepository).findById(1L);
        verify(storeRepository).existsByLatAndLng(11.0, 21.0);
        verify(storeRepository, never()).save(any());
        verifyNoInteractions(storeMapper);
    }

    @Test
    void deleteStore_success() {
        when(storeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(storeRepository).deleteById(1L);

        assertDoesNotThrow(() -> storeService.deleteStore(1L));

        verify(storeRepository).existsById(1L);
        verify(storeRepository).deleteById(1L);
    }

    @Test
    void deleteStore_notFound_throws() {
        when(storeRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            storeService.deleteStore(1L);
        });

        assertEquals("Store not found with id: 1", ex.getMessage());

        verify(storeRepository).existsById(1L);
        verify(storeRepository, never()).deleteById(any());
    }

    @Test
    void existsByLatAndLng_delegatesToRepository() {
        when(storeRepository.existsByLatAndLng(10.0, 20.0)).thenReturn(true);

        boolean exists = storeService.existsByLatAndLng(10.0, 20.0);

        assertTrue(exists);
        verify(storeRepository).existsByLatAndLng(10.0, 20.0);
    }

    @Test
    void saveEntity_delegatesToRepository() {
        Store store = new Store(null, "Store A", 10.0, 20.0);
        Store saved = new Store(1L, "Store A", 10.0, 20.0);

        when(storeRepository.save(store)).thenReturn(saved);

        Store result = storeService.saveEntity(store);

        assertEquals(1L, result.getId());
        verify(storeRepository).save(store);
    }

    @Test
    void getAllStoreEntities_delegatesToRepository() {
        List<Store> stores = List.of(
                new Store(1L, "Store A", 10.0, 20.0),
                new Store(2L, "Store B", 15.0, 25.0)
        );

        when(storeRepository.findAll()).thenReturn(stores);

        List<Store> result = storeService.getAllStoreEntities();

        assertEquals(2, result.size());
        verify(storeRepository).findAll();
    }
}
