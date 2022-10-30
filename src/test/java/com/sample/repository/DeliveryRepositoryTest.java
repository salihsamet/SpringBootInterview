package com.sample.repository;

import com.sample.dto.DeliveryDto;
import com.sample.enums.UserRole;
import com.sample.model.Delivery;
import com.sample.model.Person;
import com.sample.repositories.DeliveryRepository;
import com.sample.repositories.PersonRepository;
import com.sample.util.Calculator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@TestPropertySource(properties = { "spring.config.location = classpath:application-test.yml" })
public class DeliveryRepositoryTest {
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private PersonRepository personRepository;

    private DeliveryDto deliveryDto;
    private Delivery mockDelivery, deliveryFromRepo;
    private Person deliveryMan, customer;
    private Instant start, end;
    private long startEpoch, endEpoch;

    @BeforeEach
    void setData(){
        customer = personRepository.save(new Person( "customer", "customer@customer.com",
                "1234", UserRole.USER));
        deliveryMan = personRepository.save(new Person( "delivery", "delivery@delivery.com",
                "1235", UserRole.DELIVERY_MAN));
        startEpoch = 1665438378000L;
        endEpoch = 1665524778000L;
        start = Instant.ofEpochMilli(startEpoch);
        end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, price, deliveryMan.getId(), customer.getId());
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        mockDelivery = new Delivery( start, end, distance, price, commission, deliveryMan, customer);

        deliveryFromRepo = deliveryRepository.save(mockDelivery);
    }

    @AfterEach
    void clearDb(){
        deliveryRepository.deleteAll();
        personRepository.deleteAll();
    }

    void assertDeliveryData(Delivery expectedDelivery, Delivery actualDelivery) {
        assertEquals(expectedDelivery.getStartTime(), actualDelivery.getStartTime());
        assertEquals(expectedDelivery.getEndTime(), actualDelivery.getEndTime());
        assertEquals(expectedDelivery.getCommission(), actualDelivery.getCommission());
        assertEquals(expectedDelivery.getDeliveryMan(), actualDelivery.getDeliveryMan());
        assertEquals(expectedDelivery.getCustomer(), actualDelivery.getCustomer());
    }

    @Test
    void testSave(){
        assertDeliveryData(mockDelivery, deliveryFromRepo);
    }

    @Test
    void testFindById(){
        Optional<Delivery> deliveryFindById = deliveryRepository.findById(deliveryFromRepo.getId());
        assertDeliveryData(mockDelivery, deliveryFindById.get());
    }

    @Test
    void testGetTop3Leaderboard(){
        Person deliveryMan2 = personRepository.save(new Person("delivery2", "delivery2@delivery.com",
                "1236", UserRole.DELIVERY_MAN));
        Person deliveryMan3 = personRepository.save(new Person("delivery3", "delivery3@delivery.com",
                "1237", UserRole.DELIVERY_MAN));
        Person deliveryMan4 = personRepository.save(new Person( "delivery4", "delivery4@delivery.com",
                "1238", UserRole.DELIVERY_MAN));


        Delivery delivery2 = new Delivery( Instant.ofEpochMilli(1665438438000L),
                Instant.ofEpochMilli(1665524718000L), 2L, 2L,
                Calculator.calculateDeliveryCommission(2,2), deliveryMan2, customer);
        deliveryRepository.save(delivery2);

        Delivery delivery3 = new Delivery( Instant.ofEpochMilli(1665438438000L),      //Out of time interval
                Instant.ofEpochMilli(1665611118000L), 3L, 3L,
                Calculator.calculateDeliveryCommission(3,3), deliveryMan3, customer);
        deliveryRepository.save(delivery3);

        Delivery delivery4 = new Delivery( Instant.ofEpochMilli(1665438438000L),
                Instant.ofEpochMilli(1665524718000L), 4L, 4L,
                Calculator.calculateDeliveryCommission(4,4), deliveryMan4, customer);
        deliveryRepository.save(delivery4);

        List<Delivery> deliveryList = deliveryRepository.
                findTop3ByStartTimeGreaterThanEqualAndEndTimeLessThanEqualOrderByPriceDesc(
                        Instant.ofEpochMilli(1665438378000L), Instant.ofEpochMilli(1665524778000L));

        assertDeliveryData(delivery4, deliveryList.get(0));
        assertDeliveryData(delivery2, deliveryList.get(1));
        assertDeliveryData(mockDelivery, deliveryList.get(2));

    }

    @Test
    void testFindConflictedDeliveries(){

        Delivery delivery2 = new Delivery( Instant.ofEpochMilli(1665324778000L), //(end_time >= :endTime AND start_time < :endTime)
                Instant.ofEpochMilli(1665624778000L), 2L, 2L,
                Calculator.calculateDeliveryCommission(2,2), deliveryMan, customer);
        deliveryRepository.save(delivery2);

        Delivery delivery3 = new Delivery( Instant.ofEpochMilli(1665338378000L), //(start_time <= :startTime AND end_time > :startTime)
                Instant.ofEpochMilli(1665538378000L), 3L, 3L,
                Calculator.calculateDeliveryCommission(3,3), deliveryMan, customer);
        deliveryRepository.save(delivery3);

        Delivery delivery4 = new Delivery( Instant.ofEpochMilli(1665448378000L), //(start_time > :startTime AND end_time < :endTime)
                Instant.ofEpochMilli(1665523778000L), 4L, 4L,
                Calculator.calculateDeliveryCommission(4,4), deliveryMan, customer);
        deliveryRepository.save(delivery4);

        Delivery delivery5 = new Delivery( Instant.ofEpochMilli(1665524778000L), //valid
                Instant.ofEpochMilli(1665534778000L), 5L, 5L,
                Calculator.calculateDeliveryCommission(5,5), deliveryMan, customer);
        deliveryRepository.save(delivery5);

        List<Delivery> deliveryList = deliveryRepository.
                findConflictedTimeInvervalsForDelivery(deliveryMan.getId(),
                        Instant.ofEpochMilli(1665438378000L), Instant.ofEpochMilli(1665524778000L));

        assertDeliveryData(mockDelivery, deliveryList.get(0));
        assertDeliveryData(delivery2, deliveryList.get(1));
        assertDeliveryData(delivery3, deliveryList.get(2));
        assertDeliveryData(delivery4, deliveryList.get(3));

    }

    @Test
    void testFindDeliveryDurationGreaterThanMinutes(){
        Delivery delivery2 = new Delivery( Instant.ofEpochMilli(1665324778000L),
                Instant.ofEpochMilli(1665325778000L), 2L, 2L,
                Calculator.calculateDeliveryCommission(2,2), deliveryMan, customer);
        deliveryRepository.save(delivery2);
        List<Delivery> deliveryList = deliveryRepository.findDeliveryDurationGreaterThanMinutes(45);
        assertDeliveryData(mockDelivery, deliveryList.get(0));
        assertEquals(1, deliveryList.size()); //mockDelivery greater then 45 minutes
    }


}
