package org.app.store.mapper;

import org.app.store.dto.StoreDto;
import org.app.store.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public StoreDto toDto(Store store) {
        if (store == null) return null;
        return StoreDto.builder()
                .id(store.getId())
                .name(store.getName())
                .lat(store.getLat())
                .lng(store.getLng())
                .build();
    }

    public Store toEntity(StoreDto dto) {
        if (dto == null) return null;
        return Store.builder()
                .id(dto.getId()) 
                .name(dto.getName())
                .lat(dto.getLat())
                .lng(dto.getLng())
                .build();
    }
}
