package com.github.vinicius2335.certification.api.exception.handler;

import com.github.vinicius2335.certification.domain.exception.BusinessRulesException;
import com.github.vinicius2335.certification.domain.exception.CertificationNotFoundException;
import com.github.vinicius2335.certification.domain.exception.StudentNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NotNull MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatusCode status,
            @NotNull WebRequest request
    ) {

        List<Problem.Field> fields = new ArrayList<>();

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(
                fieldError -> fields.add(
                        new Problem.Field(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        )
                )
        );

        Problem problem = new Problem();
        problem.setTimestamp(OffsetDateTime.now());
        problem.setStatus(status.value());
        problem.setTitle("Um ou mais campos estão inválido. Faça o preenchimento correto e tente novamente.");
        problem.setFields(fields);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(BusinessRulesException.class)
    public ResponseEntity<Object> handleBusinessRulesEception(
            BusinessRulesException ex,
            WebRequest web
    ){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, web);
    }

    @ExceptionHandler(CertificationNotFoundException.class)
    public ResponseEntity<Object> handleCertificationNotFoundException(
            CertificationNotFoundException ex,
            WebRequest web
    ){
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, web);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Object> handleStudentNotFoundException(
            StudentNotFoundException ex,
            WebRequest web
    ){
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, web);
    }

    private Problem createExceptionResponseBody(RuntimeException ex, Integer status){
        Problem problem = new Problem();
        problem.setTimestamp(OffsetDateTime.now());
        problem.setStatus(status);
        problem.setTitle(ex.getMessage());

        return problem;
    }
}
