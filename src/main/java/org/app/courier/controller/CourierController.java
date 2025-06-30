package org.app.courier.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.app.courier.dto.CourierDto;
import org.app.courier.service.CourierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for Courier CRUD operations.
 */
@RestController
@RequestMapping("/api/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @PostMapping
    public ResponseEntity<CourierDto> createCourier(@Valid @RequestBody CourierDto request) {
        CourierDto created = courierService.createCourier(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourierDto> getCourierById(@PathVariable Long id) {
        return courierService.getCourierById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<CourierDto>> getAllCouriers() {
        return ResponseEntity.ok(courierService.getAllCouriers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourier(@PathVariable Long id) {
        courierService.deleteCourier(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourierDto> updateCourier(@PathVariable Long id, @Valid @RequestBody CourierDto request) {
        CourierDto updated = courierService.updateCourier(id, request);
        return ResponseEntity.ok(updated);
    }

}
