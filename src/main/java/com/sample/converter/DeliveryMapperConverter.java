package com.sample.converter;

import com.sample.dto.DeliveryDto;
import com.sample.model.Delivery;
import com.sample.util.Calculator;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.Instant;


public class DeliveryMapperConverter implements Converter<DeliveryDto, Delivery> {
    @Override
    public Delivery convert(MappingContext<DeliveryDto, Delivery> context) {
        DeliveryDto deliveryDto = context.getSource();
        Delivery delivery = context.getDestination() == null ? new Delivery() : context.getDestination();

        delivery.setDistance(deliveryDto.getDistance());
        delivery.setPrice(deliveryDto.getPrice());

        delivery.setCommission(Calculator.calculateDeliveryCommission(deliveryDto.getPrice(), deliveryDto.getDistance()));

        delivery.setStartTime(Instant.ofEpochMilli(deliveryDto.getStartTime()));
        delivery.setEndTime(Instant.ofEpochMilli(deliveryDto.getEndTime()));

        return delivery;
    }
}
