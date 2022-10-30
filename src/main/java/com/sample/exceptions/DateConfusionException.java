package com.sample.exceptions;

public class DateConfusionException  extends RuntimeException{
    public DateConfusionException(String msg) {
        super(msg);
    }
}