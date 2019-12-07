package ros.hack.api.model;

import lombok.Data;

import java.util.Map;

@Data
public class OperationInfo {
    private Map<String, String> extra;
}
