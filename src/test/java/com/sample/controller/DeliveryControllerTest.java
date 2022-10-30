package com.sample.controller;

import com.sample.dto.DeliveryDto;
import com.sample.dto.DeliveryManDto;
import com.sample.enums.UserRole;
import com.sample.exceptions.CustomerNotRegisteredException;
import com.sample.exceptions.DateConfusionException;
import com.sample.exceptions.DeliveryManNotRegisteredException;
import com.sample.exceptions.TimeIntervalConflictedException;
import com.sample.model.Delivery;
import com.sample.model.Person;
import com.sample.service.DeliveryService;
import com.sample.util.Calculator;
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

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DeliveryControllerTest {
    @MockBean
    DeliveryService deliveryService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testCreateDeliveryNullStartTime() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(null, endEpoch, distance, price, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenReturn(delivery);

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCreateDeliveryNotPositiveStartTime() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(-1L, endEpoch, distance, price, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenReturn(delivery);

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCreateDeliveryNullEndTime() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, null, distance, price, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenReturn(delivery);

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCreateDeliveryNotPositiveEndTime() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, -1L, distance, price, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenReturn(delivery);

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCreateDeliveryNullDistance() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, endEpoch, null, price, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenReturn(delivery);

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCreateDeliveryNullPrice() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, null, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenReturn(delivery);

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCreateDeliveryNullDeliveryManId() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, price, null, customer.getId());

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenReturn(delivery);

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCreateDeliveryNullCustomerId() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, price, deliveryMan.getId(), null);

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenReturn(delivery);

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCreateDeliveryTimeIntervalConflictException() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, price, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenThrow(new TimeIntervalConflictedException("The delivery man is not allowed to deliver multiple orders at the same time"));

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The delivery man is not allowed to deliver multiple orders at the same time"));
    }

    @Test
    public void testCreateDeliveryDeliveryManNotRegisteredException() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, price, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenThrow(new DeliveryManNotRegisteredException("Delivery data should only be accepted from the registered delivery man"));

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Delivery data should only be accepted from the registered delivery man"));
    }

    @Test
    public void testCreateDeliveryCustomerNotRegisteredException() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, price, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenThrow(new CustomerNotRegisteredException("Delivery data should only be accepted from the registered customer"));

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Delivery data should only be accepted from the registered customer"));
    }

    @Test
    public void testCreateDeliveryDateConfusionException() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, price, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenThrow(new DateConfusionException("Start time must be less than end time"));

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Start time must be less than end time"));
    }

    @Test
    public void testCreateDeliveryIllegalArgumentException() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, price, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenThrow(new IllegalArgumentException("An error happened creating a delivery."));

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("An error happened creating a delivery."));
    }

    @Test
    public void testCreateDelivery() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, price, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.save(any(DeliveryDto.class))).thenReturn(delivery);

        mockMvc.perform(post("/api/delivery/")
                        .content(Util.asJsonString(deliveryDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(delivery.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value(delivery.getStartTime().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endTime").value(delivery.getEndTime().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.distance").value(delivery.getDistance()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(delivery.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commission").value(delivery.getCommission()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deliveryMan").value(delivery.getDeliveryMan()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer").value(delivery.getCustomer()));
    }


    @Test
    public void testGetDeliveryByIdIllegalArgumentException() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, price, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.findById(1L)).thenThrow(new IllegalArgumentException("An error happened getting a delivery."));

        mockMvc.perform(get("/api/delivery/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("An error happened getting a delivery."));
    }

    @Test
    public void testGetDeliveryById() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, price, deliveryMan.getId(), customer.getId());

        Mockito.when(deliveryService.findById(1L)).thenReturn(delivery);

        mockMvc.perform(get("/api/delivery/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(delivery.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value(delivery.getStartTime().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endTime").value(delivery.getEndTime().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.distance").value(delivery.getDistance()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(delivery.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commission").value(delivery.getCommission()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deliveryMan").value(delivery.getDeliveryMan()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer").value(delivery.getCustomer()));
    }

    @Test
    public void testGetTop3LeaderboardIllegalArgumentException() throws Exception {

        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;

        Mockito.when(deliveryService.getTop3Leaderboard(startEpoch, endEpoch)).thenThrow(new IllegalArgumentException("An error happened getting top 3 leaderboard."));

        mockMvc.perform(get("/api/delivery/leaderboard?start_time="+startEpoch+"&end_time="+endEpoch)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("An error happened getting top 3 leaderboard."));
    }

    @Test
    public void testGetTop3Leaderboard() throws Exception {
        Person customer = new Person(1L, "customer", "customer@customer.com", "1234", UserRole.USER);
        Person deliveryMan = new Person(2L, "delivery", "delivery@delivery.com", "1235", UserRole.DELIVERY_MAN);
        long startEpoch = 1665377634000L;
        long endEpoch = 1665550434000L;
        Instant start = Instant.ofEpochMilli(startEpoch);
        Instant end = Instant.ofEpochMilli(endEpoch);
        long price = 1;
        long distance = 1;
        Double commission = Calculator.calculateDeliveryCommission(price, distance);
        Delivery delivery = new Delivery(1L, start, end, distance, price, commission, deliveryMan, customer);
        DeliveryDto deliveryDto = new DeliveryDto(startEpoch, endEpoch, distance, price, deliveryMan.getId(), customer.getId());
        DeliveryManDto deliveryManDto = new DeliveryManDto(deliveryMan.getId(), deliveryMan.getName(), deliveryMan.getEmail(), deliveryMan.getRegistrationNumber(), delivery.getCommission());
        List<DeliveryManDto> deliveryManDtoList = Arrays.asList(deliveryManDto);

        Mockito.when(deliveryService.getTop3Leaderboard(startEpoch, endEpoch)).thenReturn(deliveryManDtoList);

        mockMvc.perform(get("/api/delivery/leaderboard?start_time="+startEpoch+"&end_time="+endEpoch)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(deliveryMan.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(deliveryMan.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email").value(deliveryMan.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].registrationNumber").value(deliveryMan.getRegistrationNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].commission").value(delivery.getCommission()));
    }
}
