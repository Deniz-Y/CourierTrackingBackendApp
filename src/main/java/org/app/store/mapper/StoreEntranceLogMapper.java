package org.app.store.mapper;

import org.app.store.dto.StoreEntranceLogDto;
import org.app.store.entity.StoreEntranceLog;
import org.app.courier.entity.Courier;
import org.app.store.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreEntranceLogMapper {

    public StoreEntranceLogDto toDto(StoreEntranceLog entity) {
        if (entity == null) {
            return null;
        }

        return StoreEntranceLogDto.builder()
                .id(entity.getId())
                .courierId(entity.getCourier().getId())
                .storeId(entity.getStore().getId())
                .entranceTime(entity.getEntranceTime())
                .build();
    }

    public StoreEntranceLog toEntity(StoreEntranceLogDto dto, Courier courier, Store store) {
        if (dto == null) {
            return null;
        }

        return StoreEntranceLog.builder()
                .id(dto.getId())
                .courier(courier)
                .store(store)
                .entranceTime(dto.getEntranceTime())
                .build();
    }
}
