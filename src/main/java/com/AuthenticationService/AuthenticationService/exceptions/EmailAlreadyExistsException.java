package com.AuthenticationService.AuthenticationService.exceptions;

public class EmailAlreadyExistsException extends Exception{
    public EmailAlreadyExistsException(String message){
        super(message);
    }
}
