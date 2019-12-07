package ros.hack.api.model;

import lombok.Data;
import ros.hack.api.enums.PagingDirection;

import javax.validation.constraints.NotEmpty;

@Data
public class OperationsRequest {
    @NotEmpty(message = "{field.empty}")
    private String userId;
    private String dateFrom;
    private String dateTill;
    private String sourceId;
    private String category;
    private String status;
    private String extendedStatus;
    private int lastItemId;
    private int itemsCount;
    private PagingDirection pagingDirection;
}
