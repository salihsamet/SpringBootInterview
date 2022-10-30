package com.sample.service;

import com.sample.converter.PersonMapperConverter;
import com.sample.dto.PersonDto;
import com.sample.enums.UserRole;
import com.sample.model.Person;
import com.sample.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    PersonRepository personRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    PersonMapperConverter personMapperConverter;

    @InjectMocks
    PersonServiceImpl personService;

    private Person person;

    @BeforeEach
    void setData(){
        person = new Person(1L, "person", "person@person.com", "123", UserRole.USER);
    }

    @Test
    void testGetAll() {
        when(personRepository.findAll()).thenReturn(Arrays.asList(person));

        List<Person> personList = personService.getAll();
        assertEquals(person.getId(), personList.get(0).getId());
        assertEquals(person.getName(), personList.get(0).getName());
        assertEquals(person.getEmail(), personList.get(0).getEmail());
        assertEquals(person.getRegistrationNumber(), personList.get(0).getRegistrationNumber());
        assertEquals(person.getUserRole(), personList.get(0).getUserRole());

    }

    @Test
    void testGetAllNotExistedPerson() {
        when(personRepository.findAll()).thenReturn(new ArrayList<Person>());

        List<Person> personList = personService.getAll();
        assertEquals(0, personList.size());

    }

    @Test
    void testSave() {
        when(personRepository.save(person)).thenReturn(person);
        when(modelMapper.map(any(PersonDto.class), eq(Person.class))).thenReturn(person);

        PersonDto personDto = new PersonDto(person.getName(), person.getEmail(), person.getUserRole());
        Person personFromService = personService.save(personDto);
        assertEquals(person.getId(), personFromService.getId());
        assertEquals(person.getName(), personFromService.getName());
        assertEquals(person.getEmail(), personFromService.getEmail());
        assertEquals(person.getRegistrationNumber(), personFromService.getRegistrationNumber());
        assertEquals(person.getUserRole(), personFromService.getUserRole());

    }

    @Test
    void testFindById() {
        when(personRepository.findById(person.getId())).thenReturn(Optional.ofNullable(person));

        Person personFromService = personService.findById(person.getId());
        assertEquals(person.getId(), personFromService.getId());
        assertEquals(person.getName(), personFromService.getName());
        assertEquals(person.getEmail(), personFromService.getEmail());
        assertEquals(person.getRegistrationNumber(), personFromService.getRegistrationNumber());
        assertEquals(person.getUserRole(), personFromService.getUserRole());

    }
}
