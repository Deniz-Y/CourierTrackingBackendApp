package org.app.courier.mapper;

import org.app.courier.dto.CourierDto;
import org.app.courier.entity.Courier;
import org.springframework.stereotype.Component;

@Component
public class CourierMapper {
    public CourierDto toDto(Courier entity) {
        return CourierDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .courierNumber(entity.getCourierNumber())
                .build();
    }

    public Courier toEntity(CourierDto dto) {
        return Courier.builder()
                .id(dto.getId())
                .name(dto.getName())
                .courierNumber(dto.getCourierNumber())
                .build();
    }
}

