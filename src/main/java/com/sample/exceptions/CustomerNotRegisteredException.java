package com.sample.exceptions;

public class CustomerNotRegisteredException extends RuntimeException{
    public CustomerNotRegisteredException(String msg) {
        super(msg);
    }
}
