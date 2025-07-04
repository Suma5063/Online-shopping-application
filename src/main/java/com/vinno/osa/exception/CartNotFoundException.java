package com.vinno.osa.exception;

public class CartNotFoundException extends RuntimeException{

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CartNotFoundException(String message) {
        this.message = message;
    }
}
