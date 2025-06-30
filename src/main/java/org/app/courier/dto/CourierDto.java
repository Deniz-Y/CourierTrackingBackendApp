package org.app.courier.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourierDto {

    private Long id;

    @NotBlank(message = "Courier name is required")
    @Size(min = 2, max = 100, message = "Courier name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Courier number is required")
    @Size(min = 3, max = 50, message = "Courier number must be between 3 and 50 characters")
    private String courierNumber;
}
