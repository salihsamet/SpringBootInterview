package com.sample.converter;

import com.sample.dto.DeliveryManDto;
import com.sample.model.Delivery;
import com.sample.model.Person;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class DeliveryManMapperConverter implements Converter<Delivery, DeliveryManDto> {

    @Override
    public DeliveryManDto convert(MappingContext<Delivery, DeliveryManDto> context) {
        Delivery delivery = context.getSource();
        DeliveryManDto deliveryManDto = context.getDestination() == null ? new DeliveryManDto() : context.getDestination();

        Person deliveryMan = delivery.getDeliveryMan();
        deliveryManDto.setEmail(deliveryMan.getEmail());
        deliveryManDto.setId(deliveryMan.getId());
        deliveryManDto.setName(deliveryMan.getName());
        deliveryManDto.setRegistrationNumber(deliveryMan.getRegistrationNumber());

        deliveryManDto.setCommission(delivery.getCommission());

        return deliveryManDto;
    }
}
