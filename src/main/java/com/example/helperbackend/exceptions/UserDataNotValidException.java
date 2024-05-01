package com.example.helperbackend.exceptions;

public class UserDataNotValidException extends RuntimeException{
    public UserDataNotValidException(){
        super("User data is not valid");
    }
}
