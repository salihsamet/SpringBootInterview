package com.sample.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import com.sample.model.Person;

@RestResource(exported=false)
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

}
