package com.sample.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.sample.converter.PersonMapperConverter;
import com.sample.dto.PersonDto;
import com.sample.repositories.PersonRepository;
import com.sample.model.Person;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService{

  @Autowired
  PersonRepository personRepository;

  @Autowired
  ModelMapper modelMapper;

  @Autowired
  PersonMapperConverter personMapperConverter;

  @Override
  public List<Person> getAll() {
    List<Person> personList = new ArrayList<>();
    personRepository.findAll().forEach(personList::add);
    return personList;
  }

  public Person save(PersonDto personDto) {
    modelMapper.addConverter(personMapperConverter);
    Person person = modelMapper.map(personDto, Person.class);
    return personRepository.save(person);
  }

  @Override
  public Person findById(Long personId) {
    Optional<Person> dbPerson = personRepository.findById(personId);
    return dbPerson.orElse(null);
  }



}
