package com.sample.exceptions;

public class TimeIntervalConflictedException  extends RuntimeException{
    public TimeIntervalConflictedException(String msg) {
        super(msg);
    }
}
