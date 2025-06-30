package org.app.store.controller;

import org.app.store.dto.StoreEntranceLogDto;
import org.app.store.service.StoreEntranceLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StoreEntranceLogController.class)
class StoreEntranceLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreEntranceLogService storeEntranceLogService;

    private StoreEntranceLogDto sampleLog;

    @BeforeEach
    void setUp() {
        sampleLog = StoreEntranceLogDto.builder()
                .id(1L)
                .courierId(1L)
                .storeId(10L)
                .entranceTime(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetAllLogs() throws Exception {
        Mockito.when(storeEntranceLogService.getAllLogs())
                .thenReturn(List.of(sampleLog));

        mockMvc.perform(get("/api/store-entrance-logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].courierId").value(1L));
    }

    @Test
    void testGetLogsByCourier() throws Exception {
        Mockito.when(storeEntranceLogService.getLogsByCourier(anyLong()))
                .thenReturn(List.of(sampleLog));

        mockMvc.perform(get("/api/store-entrance-logs/courier/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].storeId").value(10L));
    }

    @Test
    void testGetLogsByStore() throws Exception {
        Mockito.when(storeEntranceLogService.getLogsByStore(anyLong()))
                .thenReturn(List.of(sampleLog));

        mockMvc.perform(get("/api/store-entrance-logs/store/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courierId").value(1L));
    }
}
