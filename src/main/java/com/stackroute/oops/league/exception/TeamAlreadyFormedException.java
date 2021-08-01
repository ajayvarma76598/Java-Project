package com.stackroute.oops.league.exception;

public class TeamAlreadyFormedException  extends RuntimeException{
    public TeamAlreadyFormedException() {
    }

    public TeamAlreadyFormedException(String message) {
        super(message);
    }
}

