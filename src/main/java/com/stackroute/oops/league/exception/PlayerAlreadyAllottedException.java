package com.stackroute.oops.league.exception;

public class PlayerAlreadyAllottedException extends RuntimeException {
    public PlayerAlreadyAllottedException() {
    }

    public PlayerAlreadyAllottedException(String message) {
        super(message);
    }
}
