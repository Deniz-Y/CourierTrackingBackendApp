package org.app.store.service;

import lombok.RequiredArgsConstructor;
import org.app.store.dto.StoreDto;
import org.app.store.entity.Store;
import org.app.store.mapper.StoreMapper;
import org.app.store.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    public List<StoreDto> getAllStores() {
        List<Store> stores = storeRepository.findAll();
        return stores.stream()
                .map(storeMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<StoreDto> getStoreById(Long id) {
        return storeRepository.findById(id)
                .map(storeMapper::toDto);
    }

    public StoreDto createStore(StoreDto dto) {
        boolean exists = storeRepository.existsByLatAndLng(dto.getLat(), dto.getLng());
        if (exists) {
            throw new IllegalArgumentException("Store at this location already exists.");
        }

        Store entity = storeMapper.toEntity(dto);
        Store saved = storeRepository.save(entity);
        return storeMapper.toDto(saved);
    }

    public StoreDto updateStore(Long id, StoreDto dto) {
        Store existing = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + id));

        if ((existing.getLat() != dto.getLat() || existing.getLng() != dto.getLng())
                && storeRepository.existsByLatAndLng(dto.getLat(), dto.getLng())) {
            throw new IllegalArgumentException("Another store at this location already exists.");
        }

        existing.setName(dto.getName());
        existing.setLat(dto.getLat());
        existing.setLng(dto.getLng());

        Store updated = storeRepository.save(existing);
        return storeMapper.toDto(updated);
    }

    public void deleteStore(Long id) {
        if (!storeRepository.existsById(id)) {
            throw new IllegalArgumentException("Store not found with id: " + id);
        }
        storeRepository.deleteById(id);
    }

    public boolean existsByLatAndLng(double lat, double lng) {
        return storeRepository.existsByLatAndLng(lat, lng);
    }

    public Store saveEntity(Store store) {
        return storeRepository.save(store);
    }
    // for internal business logic  (ex: StoreCheckService)
    public List<Store> getAllStoreEntities() {
        return storeRepository.findAll();
    }


}
