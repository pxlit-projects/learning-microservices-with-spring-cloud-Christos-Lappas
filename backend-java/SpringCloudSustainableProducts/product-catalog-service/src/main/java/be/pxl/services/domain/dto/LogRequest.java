package be.pxl.services.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogRequest {
    private LocalDateTime time = LocalDateTime.now();
    private String user = "admin";
    private Long productId;
    private String changes;
}
