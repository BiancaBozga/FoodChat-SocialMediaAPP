package com.example.demo2.exceptions;

public class ValidationException extends RuntimeException {
    private final String  errorList;

    public ValidationException(String errorList) {
        super(String.join("\n", errorList));

        this.errorList = errorList;
    }

    @Override
    public String getMessage() {
        return String.join("\n", errorList);
    }
}
