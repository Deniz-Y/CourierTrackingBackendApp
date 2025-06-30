package org.app.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.store.dto.StoreDto;
import org.app.store.service.StoreService;
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

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StoreController.class)
class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @Autowired
    private ObjectMapper objectMapper;

    private StoreDto validStore;

    @BeforeEach
    void setUp() {
        validStore = StoreDto.builder()
                .id(1L)
                .name("Sample Store")
                .lat(40.0)
                .lng(29.0)
                .build();
    }

    @Test
    void testCreateStore_ValidInput_ShouldReturnCreated() throws Exception {
        Mockito.when(storeService.createStore(any(StoreDto.class))).thenReturn(validStore);

        mockMvc.perform(post("/api/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validStore)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sample Store"))
                .andExpect(jsonPath("$.lat").value(40.0))
                .andExpect(jsonPath("$.lng").value(29.0));
    }

    @Test
    void testCreateStore_InvalidInput_ShouldReturnBadRequest() throws Exception {
        StoreDto invalidStore = StoreDto.builder()
                .name("")           // boş name - invalid
                .lat(100.0)         // invalid latitude > 90
                .lng(-200.0)        // invalid longitude < -180
                .build();

        mockMvc.perform(post("/api/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStore)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(3)));
    }

    @Test
    void testGetStoreById_Found() throws Exception {
        Mockito.when(storeService.getStoreById(1L)).thenReturn(Optional.of(validStore));

        mockMvc.perform(get("/api/stores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sample Store"));
    }

    @Test
    void testGetStoreById_NotFound() throws Exception {
        Mockito.when(storeService.getStoreById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/stores/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllStores() throws Exception {
        Mockito.when(storeService.getAllStores()).thenReturn(List.of(validStore));

        mockMvc.perform(get("/api/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Sample Store"));
    }

    @Test
    void testUpdateStore_ValidInput_ShouldReturnUpdated() throws Exception {
        Mockito.when(storeService.updateStore(eq(1L), any(StoreDto.class))).thenReturn(validStore);

        mockMvc.perform(put("/api/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validStore)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sample Store"));
    }

    @Test
    void testUpdateStore_InvalidInput_ShouldReturnBadRequest() throws Exception {
        StoreDto invalidStore = StoreDto.builder()
                .name("")        // invalid name
                .lat(95.0)       // invalid latitude
                .lng(-190.0)     // invalid longitude
                .build();

        mockMvc.perform(put("/api/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStore)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(3)));
    }

    @Test
    void testDeleteStore() throws Exception {
        mockMvc.perform(delete("/api/stores/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(storeService).deleteStore(1L);
    }
}
