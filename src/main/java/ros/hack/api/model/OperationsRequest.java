package ros.hack.api.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OperationsRequest {
    private int lastItemId;
    private int itemsCount;
    @NotEmpty
    private String userId;
    private String dateFrom;
    private String dateTill;
    private String sourceId;
    private String category;
}
