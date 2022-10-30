package com.sample.controller;


import com.sample.dto.PersonDto;
import com.sample.enums.UserRole;
import com.sample.model.Person;
import com.sample.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

  @MockBean
  PersonService personService;

  @Autowired
  MockMvc mockMvc;

  @Test
  public void testUserRegisterInvalidEmail() throws Exception {
    Person person = new Person(1L, "person", "invalid_email", "123", UserRole.USER);
    PersonDto personDto = new PersonDto(person.getName(), person.getEmail(), person.getUserRole());

    Mockito.when(personService.save(any(PersonDto.class))).thenReturn(person);

    mockMvc.perform(post("/api/person/")
                    .content(Util.asJsonString(personDto))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isInternalServerError());
  }

  @Test
  public void testUserRegisterNullEmail() throws Exception {
    Person person = new Person(1L, "person", null, "123", UserRole.USER);
    PersonDto personDto = new PersonDto(person.getName(), person.getEmail(), person.getUserRole());

    Mockito.when(personService.save(any(PersonDto.class))).thenReturn(person);

    mockMvc.perform(post("/api/person/")
                    .content(Util.asJsonString(personDto))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isInternalServerError());
  }


  @Test
  public void testUserRegisterNullUserRole() throws Exception {
    Person person = new Person(1L, "person", "person@person.com", "123", null);
    PersonDto personDto = new PersonDto(person.getName(), person.getEmail(), person.getUserRole());

    Mockito.when(personService.save(any(PersonDto.class))).thenReturn(person);

    mockMvc.perform(post("/api/person/")
                    .content(Util.asJsonString(personDto))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isInternalServerError());
  }

  @Test
  public void testUserRegisterExceptionFromService() throws Exception {
    Person person = new Person(1L, "person", "person@person.com", "123", UserRole.USER);
    PersonDto personDto = new PersonDto(person.getName(), person.getEmail(), person.getUserRole());

    Mockito.when(personService.save(any(PersonDto.class))).thenThrow(new IllegalArgumentException());

    mockMvc.perform(post("/api/person/")
                    .content(Util.asJsonString(personDto))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isInternalServerError())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("An error happened registering a person."));
  }

  @Test
  public void testUserRegister() throws Exception {
    Person person = new Person(1L, "person", "person@person.com", "123", UserRole.USER);
    PersonDto personDto = new PersonDto(person.getName(), person.getEmail(), person.getUserRole());

    Mockito.when(personService.save(any(PersonDto.class))).thenReturn(person);

    mockMvc.perform(post("/api/person/")
                    .content(Util.asJsonString(personDto))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(person.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(person.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(person.getEmail()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.registrationNumber").value(person.getRegistrationNumber()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userRole").value(person.getUserRole().getValue()));
  }

  @Test
  public void testGetAllUser() throws Exception {
    Person person1 = new Person(1L, "person", "person@person.com", "123", UserRole.USER);
    Person person2 = new Person(2L, "person2", "person2@person.com", "1234", UserRole.DELIVERY_MAN);
    List<Person> mockPersonList = Arrays.asList(person1, person2);

    Mockito.when(personService.getAll()).thenReturn(mockPersonList);

    mockMvc.perform(get("/api/person/")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(person1.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(person1.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email").value(person1.getEmail()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].registrationNumber").value(person1.getRegistrationNumber()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].userRole").value(person1.getUserRole().getValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(person2.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value(person2.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].email").value(person2.getEmail()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].registrationNumber").value(person2.getRegistrationNumber()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].userRole").value(person2.getUserRole().getValue()));
  }

  @Test
  public void testGetAllUserExceptionFromService() throws Exception {
    Person person1 = new Person(1L, "person", "person@person.com", "123", UserRole.USER);
    Person person2 = new Person(2L, "person2", "person2@person.com", "1234", UserRole.DELIVERY_MAN);
    List<Person> mockPersonList = Arrays.asList(person1, person2);

    Mockito.when(personService.getAll()).thenThrow(new IllegalArgumentException());

    mockMvc.perform(get("/api/person/")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isInternalServerError())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("An error happened getting all people."));
  }

  @Test
  public void testGetByIdUser() throws Exception {
    Person person = new Person(1L, "person", "person@person.com", "123", UserRole.USER);

    Mockito.when(personService.findById(1L)).thenReturn(person);

    mockMvc.perform(get("/api/person/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(person.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(person.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(person.getEmail()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.registrationNumber").value(person.getRegistrationNumber()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userRole").value(person.getUserRole().getValue()));
  }

  @Test
  public void testGetByIdUserExceptionFromService() throws Exception {
    Person person = new Person(1L, "person", "person@person.com", "123", UserRole.USER);

    Mockito.when(personService.findById(1L)).thenThrow(new IllegalArgumentException());

    mockMvc.perform(get("/api/person/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isInternalServerError())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("An error happened getting getting person."));
  }

}
