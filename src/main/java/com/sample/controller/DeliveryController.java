package com.sample.controller;

import com.sample.dto.DeliveryDto;
import com.sample.exceptions.CustomerNotRegisteredException;
import com.sample.exceptions.DateConfusionException;
import com.sample.exceptions.DeliveryManNotRegisteredException;
import com.sample.exceptions.TimeIntervalConflictedException;
import com.sample.model.Delivery;
import com.sample.dto.DeliveryManDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.sample.service.DeliveryService;
import java.util.List;

@RestController
public class DeliveryController {

  private static final Logger LOG = LoggerFactory.getLogger(DeliveryController.class);

  @Autowired
  DeliveryService deliveryService;

  @PostMapping(path ="/api/delivery")
  public ResponseEntity<Delivery> createNewDelivery(@Validated @RequestBody DeliveryDto deliveryDto) throws Exception {
    try {
      Delivery delivery = deliveryService.save(deliveryDto);
      return ResponseEntity.ok(delivery);

    } catch (TimeIntervalConflictedException timeIntervalConflictedException){
      LOG.error(timeIntervalConflictedException.getMessage() + "Start time: " +
              deliveryDto.getStartTime() + " End time: " + deliveryDto.getEndTime());
      throw new TimeIntervalConflictedException(timeIntervalConflictedException.getMessage());

    } catch (DeliveryManNotRegisteredException deliveryManNotRegisteredException) {
      LOG.error(deliveryManNotRegisteredException.getMessage());
      throw new DeliveryManNotRegisteredException(deliveryManNotRegisteredException.getMessage());

    } catch (CustomerNotRegisteredException customerNotRegisteredException) {
      LOG.error(customerNotRegisteredException.getMessage());
      throw new CustomerNotRegisteredException(customerNotRegisteredException.getMessage());

    } catch (DateConfusionException dateConfusionException) {
      LOG.error(dateConfusionException.getMessage());
      throw new DateConfusionException(dateConfusionException.getMessage());

  } catch (Exception anyException){
      LOG.error("An error happened creating a delivery. " + deliveryDto.toString());
      throw new Exception("An error happened creating a delivery.");
    }
  }

  @GetMapping(path = "/api/delivery/{delivery-id}")
  public ResponseEntity<Delivery> getDeliveryById(@PathVariable(name="delivery-id",required=true)Long deliveryId) throws Exception {
    try {
      Delivery delivery = deliveryService.findById(deliveryId);
      return ResponseEntity.ok(delivery);
    } catch (Exception anyException){
      LOG.error("An error happened getting a delivery that id: " + deliveryId);
      throw new Exception("An error happened getting a delivery.");
    }
  }

  @GetMapping(path = "/api/delivery/leaderboard")
  public ResponseEntity<List<DeliveryManDto>> getTop3Leaderboard(@RequestParam("start_time")Long start_time, @RequestParam("end_time")Long end_time) throws Exception {
    try {
      List<DeliveryManDto> deliveryManDtoList = deliveryService.getTop3Leaderboard(start_time, end_time);
      return ResponseEntity.ok(deliveryManDtoList);
    } catch (Exception anyException){
      LOG.error("An error happened getting top 3 leaderboard.");
      throw new Exception("An error happened getting top 3 leaderboard.");
    }
  }
}
