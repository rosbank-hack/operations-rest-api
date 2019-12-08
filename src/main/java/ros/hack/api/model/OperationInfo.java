package ros.hack.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class OperationInfo {
    private long itemId;
    private String operationId;
    private String service;
    private String name;
    private String date;
    private String status;
    private String extendedStatus;
    private BigDecimal amount;
    private String currency;
    private String sourceId;
    private String mcc;
    private String extra;
}
