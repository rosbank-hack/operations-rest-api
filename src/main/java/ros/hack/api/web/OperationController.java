package ros.hack.api.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ros.hack.api.model.OperationInfo;
import ros.hack.api.model.OperationsRequest;
import ros.hack.api.service.OperationService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/operations")
public class OperationController {
    private final OperationService operationService;

    @PostMapping(
            path = "/search",
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public List<OperationInfo> findOperations(@RequestBody @Valid OperationsRequest request) {
        return operationService.findOperations(request);
    }

    @GetMapping(
            path = "/{id}",
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public OperationInfo getOperation(@PathVariable Long id) {
        return operationService.getOperation(id);
    }
}
