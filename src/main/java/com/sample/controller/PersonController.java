package com.sample.controller;

import java.util.List;

import com.sample.dto.PersonDto;
import com.sample.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.sample.service.PersonService;

@RestController
public class PersonController {

  private static final Logger LOG = LoggerFactory.getLogger(PersonController.class);


  @Autowired
  PersonService personService;

  @PostMapping(path = "/api/person")
  public ResponseEntity<Person> register(@Validated @RequestBody PersonDto p) throws Exception {
    try {
      return ResponseEntity.ok(personService.save(p));
    } catch (Exception anyException) {
      LOG.error("An error happened registering a person. " + p.toString());
      throw new Exception("An error happened registering a person.");
    }
  }

  @GetMapping(path = "/api/person")
  public ResponseEntity<List<Person>> getAllPersons() throws Exception {
    try {
      return ResponseEntity.ok(personService.getAll());
    } catch (Exception anyException) {
      LOG.error("An error happened getting all people. " + anyException.getMessage());
      throw new Exception("An error happened getting all people.");
    }
  }

  @GetMapping(path = "/api/person/{person-id}")
  public ResponseEntity<Person> getPersonById(@PathVariable(name="person-id", required=true)Long personId) throws Exception {
    try {
      Person person = personService.findById(personId);
      return ResponseEntity.ok(person);
    } catch (Exception anyException) {
      LOG.error("An error happened getting person that id: " + personId);
      throw new Exception("An error happened getting getting person.");
    }
  }

}
