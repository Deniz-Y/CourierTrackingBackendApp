package org.app.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreEntranceLogDto {
    private Long id;
    private Long courierId;
    private Long storeId;
    private LocalDateTime entranceTime;
}
