package com.github.vinicius2335.certification.domain.exception;

import java.io.Serial;

public class StudentNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7372179593319267884L;

    public StudentNotFoundException(String message) {
        super(message);
    }
}
