package com.sample.service;

import com.sample.converter.DeliveryManMapperConverter;
import com.sample.converter.DeliveryMapperConverter;
import com.sample.dto.DeliveryDto;
import com.sample.dto.DeliveryManDto;
import com.sample.enums.UserRole;
import com.sample.exceptions.CustomerNotRegisteredException;
import com.sample.exceptions.DateConfusionException;
import com.sample.exceptions.DeliveryManNotRegisteredException;
import com.sample.exceptions.TimeIntervalConflictedException;
import com.sample.model.Delivery;
import com.sample.model.Person;
import com.sample.repositories.DeliveryRepository;
import com.sample.util.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {
    @Mock
    DeliveryRepository deliveryRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    DeliveryMapperConverter deliveryMapperConverter;

    @Mock
    DeliveryManMapperConverter deliveryManMapperConverter;

    @Mock
    PersonService personService;

    @InjectMocks
    DeliveryServiceImpl deliveryService;

    private DeliveryDto deliveryDto;
    private Delivery mockDelivery;
    private Person deliveryMan, customer;
    private Instant start, end;
    private long startEpoch, endEpoch;

    @BeforeEach
    void setData(){
        customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        startEpoch = 1665377634000L;
        endEpoch = 1665550434000L;
        start = Instant.ofEpochMilli(startEpoch);
        end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, price, deliveryMan.getId(), customer.getId());
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        mockDelivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);


    }

    @Test
    void testCreateDeliveryConflictedDelivery(){
        List<Delivery> deliveries = Arrays.asList(mockDelivery);
        when(deliveryRepository.findConflictedTimeInvervalsForDelivery(deliveryMan.getId(), start, end)).thenReturn(deliveries);

        when(personService.findById(customer.getId())).thenReturn(customer);
        when(personService.findById(deliveryMan.getId())).thenReturn(deliveryMan);

        Exception exception = assertThrows(TimeIntervalConflictedException.class, () -> {
            deliveryService.save(deliveryDto);
        });
        String expectedMessage = "The delivery man is not allowed to deliver multiple orders at the same time";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testCreateDeliveryNotRegisteredDeliveryMan(){
        when(personService.findById(customer.getId())).thenReturn(customer);
        when(personService.findById(deliveryMan.getId())).thenReturn(null);

        Exception exception = assertThrows(DeliveryManNotRegisteredException.class, () -> {
            deliveryService.save(deliveryDto);
        });
        String expectedMessage = "Delivery data should only be accepted from the registered delivery man";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testCreateDeliveryNotRegisteredCustomer(){
        when(personService.findById(customer.getId())).thenReturn(null);
        when(personService.findById(deliveryMan.getId())).thenReturn(deliveryMan);

        Exception exception = assertThrows(CustomerNotRegisteredException.class, () -> {
            deliveryService.save(deliveryDto);
        });
        String expectedMessage = "Delivery data should only be accepted from the registered customer";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testCreateDeliveryDateConfusion(){
        when(personService.findById(customer.getId())).thenReturn(customer);
        when(personService.findById(deliveryMan.getId())).thenReturn(deliveryMan);

        deliveryDto.setEndTime(deliveryDto.getStartTime());
        Exception exception = assertThrows(DateConfusionException.class, () -> {
            deliveryService.save(deliveryDto);
        });
        String expectedMessage = "Start time must be less than end time";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testCreateDelivery(){
        when(personService.findById(customer.getId())).thenReturn(customer);
        when(personService.findById(deliveryMan.getId())).thenReturn(deliveryMan);

        when(deliveryRepository.findConflictedTimeInvervalsForDelivery(deliveryMan.getId(), start, end)).thenReturn(new ArrayList<Delivery>());
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(mockDelivery);

        when(modelMapper.map(any(DeliveryDto.class), eq(Delivery.class))).thenReturn(mockDelivery);

        Delivery delivery =  deliveryService.save(deliveryDto);
        assertEquals(mockDelivery.getId(), delivery.getId());
        assertEquals(mockDelivery.getStartTime(), delivery.getStartTime());
        assertEquals(mockDelivery.getEndTime(), delivery.getEndTime());
        assertEquals(mockDelivery.getCommission(), delivery.getCommission());
        assertEquals(mockDelivery.getDeliveryMan(), delivery.getDeliveryMan());
        assertEquals(mockDelivery.getCustomer(), delivery.getCustomer());

    }

    @Test
    void testFindByIdDelivery(){

        when(deliveryRepository.findById(mockDelivery.getId())).thenReturn(Optional.ofNullable(mockDelivery));

        Delivery delivery =  deliveryService.findById(mockDelivery.getId());
        assertEquals(mockDelivery.getId(), delivery.getId());
        assertEquals(mockDelivery.getStartTime(), delivery.getStartTime());
        assertEquals(mockDelivery.getEndTime(), delivery.getEndTime());
        assertEquals(mockDelivery.getCommission(), delivery.getCommission());
        assertEquals(mockDelivery.getDeliveryMan(), delivery.getDeliveryMan());
        assertEquals(mockDelivery.getCustomer(), delivery.getCustomer());

    }

    @Test
    void testFindByIdNotExistedDelivery(){

        when(deliveryRepository.findById(mockDelivery.getId())).thenReturn(Optional.ofNullable(null));

        Delivery delivery =  deliveryService.findById(mockDelivery.getId());
        assertNull(delivery);

    }

    @Test
    void testGetTop3Leaderboard(){

        DeliveryManDto deliveryManDto = new DeliveryManDto(deliveryMan.getId(), deliveryMan.getName(),
                deliveryMan.getEmail(), deliveryMan.getRegistrationNumber(), mockDelivery.getCommission());
        when(modelMapper.map(any(Delivery.class), eq(DeliveryManDto.class))).thenReturn(deliveryManDto);

        List<Delivery> deliveries = Arrays.asList(mockDelivery);
        when(deliveryRepository.findTop3ByStartTimeGreaterThanEqualAndEndTimeLessThanEqualOrderByPriceDesc(start, end))
                .thenReturn(deliveries);

        List<DeliveryManDto> top3Leaderboard = deliveryService.getTop3Leaderboard(startEpoch, endEpoch);
        assertEquals(deliveryMan.getId(), top3Leaderboard.get(0).getId());
        assertEquals(deliveryMan.getName(), top3Leaderboard.get(0).getName());
        assertEquals(deliveryMan.getEmail(), top3Leaderboard.get(0).getEmail());
        assertEquals(deliveryMan.getRegistrationNumber(), top3Leaderboard.get(0).getRegistrationNumber());
        assertEquals(mockDelivery.getCommission(), top3Leaderboard.get(0).getCommission());

    }

    @Test
    void testGetTop3LeaderboardDateConfusionException(){

        Exception exception = assertThrows(DateConfusionException.class, () -> {
            deliveryService.getTop3Leaderboard(startEpoch, startEpoch);
        });
    }

    @Test
    void testFindDeliveryDurationGreaterThanMinutes(){
        List<Delivery> deliveries = Arrays.asList(mockDelivery);
        when(deliveryRepository.findDeliveryDurationGreaterThanMinutes(45))
                .thenReturn(deliveries);
        List<Delivery> deliveriesGreaterThan45Min = deliveryRepository.findDeliveryDurationGreaterThanMinutes(45);
        assertEquals(mockDelivery.getId(), deliveriesGreaterThan45Min.get(0).getId());
        assertEquals(mockDelivery.getStartTime(), deliveriesGreaterThan45Min.get(0).getStartTime());
        assertEquals(mockDelivery.getEndTime(), deliveriesGreaterThan45Min.get(0).getEndTime());
        assertEquals(mockDelivery.getCommission(), deliveriesGreaterThan45Min.get(0).getCommission());
        assertEquals(mockDelivery.getDeliveryMan(), deliveriesGreaterThan45Min.get(0).getDeliveryMan());
        assertEquals(mockDelivery.getCustomer(), deliveriesGreaterThan45Min.get(0).getCustomer());
    }


}
