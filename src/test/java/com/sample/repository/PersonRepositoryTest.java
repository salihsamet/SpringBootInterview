package com.sample.repository;

import com.sample.enums.UserRole;
import com.sample.model.Person;
import com.sample.repositories.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@TestPropertySource(properties = { "spring.config.location = classpath:application-test.yml" })
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    private Person person, personFromRepo;

    @BeforeEach
    void setData(){
        person = new Person("person", "person@person.com", "123", UserRole.USER);
        personFromRepo = personRepository.save(person);
    }

    @AfterEach
    void clearDb(){
        personRepository.deleteAll();
    }

    @Test
    void testSavePerson() {

        assertEquals(person.getName(), personFromRepo.getName());
        assertEquals(person.getEmail(), personFromRepo.getEmail());
        assertEquals(person.getRegistrationNumber(), personFromRepo.getRegistrationNumber());
        assertEquals(person.getUserRole(), personFromRepo.getUserRole());

    }

    @Test
    void testGetAllPerson() {
        List<Person> personList = (List<Person>) personRepository.findAll();
        assertEquals(person.getName(), personList.get(0).getName());
        assertEquals(person.getEmail(), personList.get(0).getEmail());
        assertEquals(person.getRegistrationNumber(), personList.get(0).getRegistrationNumber());
        assertEquals(person.getUserRole(), personList.get(0).getUserRole());

    }

    @Test
    void testFindByIdPerson() {
        Optional<Person> personById = personRepository.findById(personFromRepo.getId());
        assertEquals(person.getName(), personById.get().getName());
        assertEquals(person.getEmail(), personById.get().getEmail());
        assertEquals(person.getRegistrationNumber(), personById.get().getRegistrationNumber());
        assertEquals(person.getUserRole(), personById.get().getUserRole());

    }
}
