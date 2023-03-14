package th.co.pentasol.pcs.master.exception;

import th.co.pentasol.pcs.master.model.api.ApiMessage;
import th.co.pentasol.pcs.master.model.api.ApiResponse;
import th.co.pentasol.pcs.master.util.ApiStatus;
import th.co.pentasol.pcs.master.component.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings({"ALL", "unchecked"})
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponse error = new ApiResponse();
        error.setStatus(status.value());
        error.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> messageSource.getMessage(x, Locale.ENGLISH))
                .collect(Collectors.toList());
        //error.setMessages(new ApiMessage(Message.WARNING, !Objects.isNull(errors) && errors.size() == 1 ? errors.get(0) : null, !Objects.isNull(errors) && errors.size() > 1 ? errors : null));
        return new ResponseEntity<>(error, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponse error = new ApiResponse();
        error.setStatus(status.value());
        error.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            List<ApiMessage> message = new ArrayList<>();
            message.add(new ApiMessage(Message.ERROR, messageSource.getMessage("msg.internal.server.error", null, Locale.ENGLISH)));
            error.setMessages(message);
        }
        return new ResponseEntity(error, status);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation( ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        log.error(ex.getMessage(),ex);

        ApiResponse error = new ApiResponse();
        error.setStatus(ApiStatus.STATUS_BAD_REQUEST);
        error.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        //error.setMessages(new ApiMessage(Message.WARNING, !Objects.isNull(errors) && errors.size() == 1 ? errors.get(0) : null, !Objects.isNull(errors) && errors.size() > 1 ? errors : null));
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }

    @ExceptionHandler//({SQLSyntaxErrorException.class, FileNotFoundException.class, Jasper})
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, WebRequest request) {
        log.error(ex.getMessage(),ex);
        ApiResponse error = new ApiResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        error.setError(!Objects.isNull(ex) ? ex.getMessage() : null);

        List<ApiMessage> message = new ArrayList<>();
        message.add(new ApiMessage(Message.ERROR, messageSource.getMessage("msg.internal.server.error", null, Locale.ENGLISH)));

        error.setMessages(message);
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ ServiceException.class })
    public ResponseEntity<Object> handleServiceForbiddenException(ServiceException ex, WebRequest request) {
        log.error(ex.getMessage(),ex);
        ApiResponse error = new ApiResponse();
        error.setStatus(ex.getHttpStatus());

        List<ApiMessage> message = new ArrayList<>();
        message.add(ex.getApiMessage());

        error.setMessages(message);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }
}