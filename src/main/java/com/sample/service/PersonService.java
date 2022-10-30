package com.sample.service;

import java.util.List;

import com.sample.dto.PersonDto;
import com.sample.model.Person;

public interface PersonService {
  public List<Person> getAll();

  public Person save(PersonDto p);

  public Person findById(Long personId);

}
