package com.sample.config;

import com.sample.converter.DeliveryManMapperConverter;
import com.sample.converter.DeliveryMapperConverter;
import com.sample.converter.PersonMapperConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public DeliveryMapperConverter deliveryMapperConverter() {
        return new DeliveryMapperConverter();
    }

    @Bean
    public DeliveryManMapperConverter deliveryManMapperConverter() {
        return new DeliveryManMapperConverter();
    }

    @Bean
    public PersonMapperConverter personMapperConverter() {
        return new PersonMapperConverter();
    }
}