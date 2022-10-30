package com.sample.exceptions;

public class DeliveryManNotRegisteredException extends RuntimeException{
    public DeliveryManNotRegisteredException(String msg) {
        super(msg);
    }
}
