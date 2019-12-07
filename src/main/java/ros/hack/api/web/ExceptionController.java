package ros.hack.api.web;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ros.hack.api.model.ErrorResponse;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;

@RestControllerAdvice
public class ExceptionController {
    private static final Logger logger = getLogger(ExceptionController.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleBadRequest(MethodArgumentNotValidException exception) {
        String validationErrors;
        if (!exception.getBindingResult().getFieldErrors().isEmpty()) {
            validationErrors = prepareValidationErrors(exception.getBindingResult().getFieldErrors());
        } else if (exception.getBindingResult().getGlobalError() != null) {
            validationErrors = exception.getBindingResult().getGlobalError().getDefaultMessage();
        } else {
            validationErrors = exception.getMessage();
        }
        return new ErrorResponse(validationErrors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse handleBadRequest(BindException exception) {
        String validationErrors = prepareValidationErrors(exception.getBindingResult().getFieldErrors());
        return new ErrorResponse(validationErrors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse handleBadRequest(MethodArgumentTypeMismatchException exception) {
        String validationErrors = prepareValidationTypeErrors(exception.getParameter());
        return new ErrorResponse(validationErrors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleBadRequest(HttpMessageNotReadableException exception) {
        final Throwable cause = exception.getCause();
        if (cause instanceof InvalidFormatException) {
            final InvalidFormatException invalidFormatException = (InvalidFormatException) cause;
            final String parameterName = !invalidFormatException.getPath().isEmpty()
                    ? invalidFormatException.getPath().get(0).getFieldName()
                    : "UNKNOWN";
            final String parameterType = invalidFormatException.getTargetType().getSimpleName();
            return new ErrorResponse(prepareValidationTypeErrors(parameterName, parameterType));
        } else if (cause instanceof JsonParseException) {
            return new ErrorResponse(format("JSON parse error: [%s]", ((JsonParseException) cause).getOriginalMessage()));
        } else if (cause instanceof JsonMappingException) {
            return new ErrorResponse(format("JSON mapping error: [%s]", ((JsonMappingException) cause).getOriginalMessage()));
        } else {
            return internalError(exception);
        }
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorResponse handleMethodNotAllowed(HttpRequestMethodNotSupportedException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleNotFound(EntityNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse internalError(Exception exception) {
        logger.error("", exception);
        return new ErrorResponse("Internal Server Error");
    }

    private String prepareValidationErrors(@Nonnull List<FieldError> errors) {
        return errors.stream()
                .map(err -> format("Field '%s' has wrong value: [%s]", err.getField(), err.getDefaultMessage()))
                .collect(Collectors.joining(";"));
    }

    private String prepareValidationTypeErrors(@Nonnull MethodParameter methodParameter) {
        String parameterName = methodParameter.getParameterName();
        if (isEmpty(parameterName)) {
            parameterName = methodParameter.getParameter().getName();
        }
        return prepareValidationTypeErrors(parameterName, methodParameter.getParameterType().getSimpleName());
    }

    private String prepareValidationTypeErrors(@Nonnull String parameterName, @Nonnull String parameterTypeName) {
        return format("Field '%s' has wrong type. Required %s", parameterName, parameterTypeName);
    }
}
