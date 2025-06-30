package org.app.courier.mapper;

import org.app.courier.dto.CourierLocationDto;
import org.app.courier.entity.Courier;
import org.app.courier.entity.CourierLocation;
import org.springframework.stereotype.Component;

@Component
public class CourierLocationMapper {

    public CourierLocationDto toDto(CourierLocation entity) {
        return CourierLocationDto.builder()
                .courierId(entity.getCourier().getId())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .timestamp(entity.getTimestamp())
                .build();
    }

    public CourierLocation toEntity(CourierLocationDto dto, Courier courier) {
        return CourierLocation.builder()
                .courier(courier)
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .timestamp(dto.getTimestamp())
                .build();
    }
}
