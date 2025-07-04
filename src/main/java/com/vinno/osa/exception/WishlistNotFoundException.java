package com.vinno.osa.exception;

public class WishlistNotFoundException extends RuntimeException{

    private String message;

    public WishlistNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
