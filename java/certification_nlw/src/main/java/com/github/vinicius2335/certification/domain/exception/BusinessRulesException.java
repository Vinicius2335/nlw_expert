package com.github.vinicius2335.certification.domain.exception;

import java.io.Serial;

public class BusinessRulesException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -7895772760958039223L;

    public BusinessRulesException(String message) {
        super(message);
    }
}
