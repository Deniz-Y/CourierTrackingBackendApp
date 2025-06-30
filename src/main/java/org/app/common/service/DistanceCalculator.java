package org.app.common.service;

//     * Calculates the distance between two coordinates in meters
//Strategy Pattern
public interface DistanceCalculator {

    double calculateDistance(double lat1, double lng1, double lat2, double lng2);
}
