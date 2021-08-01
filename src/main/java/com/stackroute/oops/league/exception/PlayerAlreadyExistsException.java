package com.stackroute.oops.league.exception;

public class PlayerAlreadyExistsException  extends RuntimeException{
    public PlayerAlreadyExistsException() {
    }

    public PlayerAlreadyExistsException(String message) {
        super(message);
    }
}

