package org.app.courier.controller;

import jakarta.validation.Valid;
import org.app.courier.dto.CourierLocationDto;
import org.app.courier.service.CourierLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courier-locations")
@RequiredArgsConstructor
public class CourierLocationController {

    private final CourierLocationService courierLocationService;

    @PostMapping
    public ResponseEntity<CourierLocationDto> addLocation(@Valid @RequestBody CourierLocationDto request) {
        CourierLocationDto saved = courierLocationService.addCourierLocation(request);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{courierId}/total-distance")
    public ResponseEntity<Double> getTotalTravelDistance(@PathVariable Long courierId) {
        double distance = courierLocationService.getTotalDistance(courierId);
        return ResponseEntity.ok(distance);
    }
}
