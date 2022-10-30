package com.sample.converter;

import com.sample.dto.PersonDto;
import com.sample.model.Person;
import com.sample.util.Generator;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;


public class PersonMapperConverter implements Converter<PersonDto, Person> {
    @Override
    public Person convert(MappingContext<PersonDto, Person> context) {
        PersonDto personDto = context.getSource();
        Person person = context.getDestination() == null ? new Person() : context.getDestination();

        person.setRegistrationNumber(Generator.generateUUID());
        person.setEmail(personDto.getEmail());
        person.setName(personDto.getName());
        person.setUserRole(personDto.getUserRole());

        return person;
    }
}
