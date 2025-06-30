package org.app.courier.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.courier.dto.CourierLocationDto;
import org.app.courier.service.CourierLocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourierLocationController.class)
class CourierLocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierLocationService courierLocationService;

    @Autowired
    private ObjectMapper objectMapper;

    private CourierLocationDto validDto;

    @BeforeEach
    void setUp() {
        validDto = CourierLocationDto.builder()
                .courierId(1L)
                .latitude(40.0)
                .longitude(29.0)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    void testAddLocation_ValidInput_ShouldReturnOk() throws Exception {
        Mockito.when(courierLocationService.addCourierLocation(any(CourierLocationDto.class)))
                .thenReturn(validDto);

        mockMvc.perform(post("/api/courier-locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courierId").value(1L))
                .andExpect(jsonPath("$.latitude").value(40.0))
                .andExpect(jsonPath("$.longitude").value(29.0));
    }

    @Test
    void testAddLocation_InvalidInput_ShouldReturnBadRequest() throws Exception {
        CourierLocationDto invalidDto = CourierLocationDto.builder()
                .courierId(null)            // null -> validation error
                .latitude(100.0)            // invalid latitude > 90
                .longitude(-200.0)          // invalid longitude < -180
                .timestamp(null)            // null timestamp -> validation error
                .build();

        mockMvc.perform(post("/api/courier-locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                // We check that there are at least 4 validation errors
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(4)));
    }

    @Test
    void testGetTotalTravelDistance() throws Exception {
        Mockito.when(courierLocationService.getTotalDistance(1L)).thenReturn(1234.56);

        mockMvc.perform(get("/api/courier-locations/1/total-distance"))
                .andExpect(status().isOk())
                .andExpect(content().string("1234.56"));
    }
}
