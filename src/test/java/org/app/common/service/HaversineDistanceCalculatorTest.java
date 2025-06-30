package org.app.common.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HaversineDistanceCalculatorTest {

    private final HaversineDistanceCalculator calculator = new HaversineDistanceCalculator();

    @Test
    void testCalculateDistance_SamePoint_ShouldReturnZero() {
        double lat = 40.0;
        double lng = 29.0;

        double distance = calculator.calculateDistance(lat, lng, lat, lng);

        assertEquals(0, distance, 0.0001, "Distance between same points should be zero");
    }

    @Test
    void testCalculateDistance_KnownPoints_ShouldReturnApproxDistance() {
        // İstanbul (lat=41.0082, lng=28.9784)
        // Ankara (lat=39.9334, lng=32.8597)
        double distance = calculator.calculateDistance(41.0082, 28.9784, 39.9334, 32.8597);

        // İstanbul-Ankara distance: 349 km (349355.7392862521 m)
        assertEquals(349355.7392862521, distance, 1000, "Distance should be approximately 351 km");
    }

    @Test
    void testCalculateDistance_OppositePoints() {
        // North Pole (90, 0) and South Pole (-90, 0)
        double distance = calculator.calculateDistance(90, 0, -90, 0);

        // Earth diameter 20015 km = 20015000 m
        assertEquals(20015000, distance, 10000, "Distance between poles should be about Earth's diameter");
    }
}
