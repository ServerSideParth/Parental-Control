package com.luv2code.emailVerification.demo.exception;

public class UserNotExistsException extends RuntimeException{
    public UserNotExistsException(String message){
        super(message);
    }
}
