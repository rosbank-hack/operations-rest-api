package ros.hack.api.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class OperationsRequest {
    @NotEmpty
    private String userId;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTill;
    private String sourceId;
}
