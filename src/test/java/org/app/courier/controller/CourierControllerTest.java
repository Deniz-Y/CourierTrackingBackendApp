package org.app.courier.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.courier.dto.CourierDto;
import org.app.courier.service.CourierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourierController.class)
class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierService courierService;

    @Autowired
    private ObjectMapper objectMapper;

    private CourierDto validCourier;

    @BeforeEach
    void setUp() {
        validCourier = CourierDto.builder()
                .id(1L)
                .name("John Doe")
                .courierNumber("C12345")
                .build();
    }

    @Test
    void testCreateCourier_ValidInput_ShouldReturnCreated() throws Exception {
        Mockito.when(courierService.createCourier(any(CourierDto.class))).thenReturn(validCourier);

        mockMvc.perform(post("/api/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCourier)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.courierNumber").value("C12345"));
    }

    @Test
    void testCreateCourier_InvalidInput_ShouldReturnBadRequest() throws Exception {
        CourierDto invalidCourier = new CourierDto(); // Boş name ve courierNumber

        mockMvc.perform(post("/api/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCourier)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(2)); // İki alan da boş
    }

    @Test
    void testGetCourierById_Found() throws Exception {
        Mockito.when(courierService.getCourierById(1L)).thenReturn(Optional.of(validCourier));

        mockMvc.perform(get("/api/couriers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testGetCourierById_NotFound() throws Exception {
        Mockito.when(courierService.getCourierById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/couriers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllCouriers() throws Exception {
        Mockito.when(courierService.getAllCouriers()).thenReturn(List.of(validCourier));

        mockMvc.perform(get("/api/couriers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testDeleteCourier() throws Exception {
        mockMvc.perform(delete("/api/couriers/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(courierService).deleteCourier(1L);
    }

    @Test
    void testUpdateCourier_ValidInput_ShouldReturnUpdated() throws Exception {
        Mockito.when(courierService.updateCourier(eq(1L), any(CourierDto.class)))
                .thenReturn(validCourier);

        mockMvc.perform(put("/api/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCourier)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testUpdateCourier_InvalidInput_ShouldReturnBadRequest() throws Exception {
        CourierDto invalidDto = new CourierDto();
        mockMvc.perform(put("/api/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
