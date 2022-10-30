package com.sample.util;

public class Calculator {

    public static Double calculateDeliveryCommission(long orderPrice, long distance){
        return orderPrice * 0.05 + distance * 0.5;
    }
}
