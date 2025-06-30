package org.app.courier.service;

import lombok.RequiredArgsConstructor;
import org.app.courier.dto.CourierDto;
import org.app.courier.entity.Courier;
import org.app.courier.mapper.CourierMapper;
import org.app.courier.repository.CourierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierMapper courierMapper;
    private final CourierRepository courierRepository;

    public CourierDto createCourier(CourierDto request) {
        if (courierRepository.existsByCourierNumber(request.getCourierNumber())) {
            throw new IllegalArgumentException("Courier number already exists.");
        }
        Courier saved = courierRepository.save(courierMapper.toEntity(request));
        return courierMapper.toDto(saved);
    }

    public Optional<CourierDto> getCourierById(Long id) {
        return courierRepository.findById(id)
                .map(courierMapper::toDto);
    }

    public List<CourierDto> getAllCouriers() {
        return courierRepository.findAll()
                .stream()
                .map(courierMapper::toDto)
                .toList();
    }

    public void deleteCourier(Long id) {
        if (!courierRepository.existsById(id)) {
            throw new IllegalArgumentException("Courier not found with ID: " + id);
        }
        courierRepository.deleteById(id);
    }

    public CourierDto updateCourier(Long id, CourierDto request) {
        Courier courier = courierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Courier not found with ID: " + id));

        if (!courier.getCourierNumber().equals(request.getCourierNumber()) &&
                courierRepository.existsByCourierNumber(request.getCourierNumber())) {
            throw new IllegalArgumentException("Courier number already exists.");
        }

        courier.setName(request.getName());
        courier.setCourierNumber(request.getCourierNumber());

        Courier updated = courierRepository.save(courier);
        return courierMapper.toDto(updated);
    }
}
