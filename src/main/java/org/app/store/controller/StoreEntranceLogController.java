package org.app.store.controller;

import org.app.store.dto.StoreEntranceLogDto;
import org.app.store.service.StoreEntranceLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store-entrance-logs")
public class StoreEntranceLogController {

    private final StoreEntranceLogService storeEntranceLogService;

    @GetMapping
    public ResponseEntity<List<StoreEntranceLogDto>> getAllLogs() {
        List<StoreEntranceLogDto> logs = storeEntranceLogService.getAllLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/courier/{courierId}")
    public ResponseEntity<List<StoreEntranceLogDto>> getLogsByCourier(@PathVariable Long courierId) {
        List<StoreEntranceLogDto> logs = storeEntranceLogService.getLogsByCourier(courierId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<StoreEntranceLogDto>> getLogsByStore(@PathVariable Long storeId) {
        List<StoreEntranceLogDto> logs = storeEntranceLogService.getLogsByStore(storeId);
        return ResponseEntity.ok(logs);
    }
}
