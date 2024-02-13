package com.github.vinicius2335.certification.domain.exception;

import java.io.Serial;

public class CertificationNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -7428639877775295972L;

    public CertificationNotFoundException(String message) {
        super(message);
    }
}
