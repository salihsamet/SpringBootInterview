package com.sample.service;

import java.time.Instant;
import java.util.*;

import com.sample.converter.DeliveryManMapperConverter;
import com.sample.converter.DeliveryMapperConverter;
import com.sample.dto.DeliveryDto;
import com.sample.exceptions.CustomerNotRegisteredException;
import com.sample.exceptions.DateConfusionException;
import com.sample.exceptions.DeliveryManNotRegisteredException;
import com.sample.exceptions.TimeIntervalConflictedException;
import com.sample.model.Person;
import com.sample.repositories.DeliveryRepository;
import com.sample.model.Delivery;
import com.sample.dto.DeliveryManDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl implements DeliveryService {

  @Autowired
  DeliveryRepository deliveryRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private DeliveryMapperConverter deliveryMapperConverter;

  @Autowired
  private DeliveryManMapperConverter deliveryManMapperConverter;

  private PersonService personService;

  @Autowired
  public void setPersonService(PersonService personService) {
    this.personService = personService;
  }

  @Override
  public Delivery save(DeliveryDto deliveryDto) {
    Person deliveryMan = personService.findById(deliveryDto.getDelivery_man_id());
    Person customer = personService.findById(deliveryDto.getCustomer_id());

    Instant currentDeliveryEndTime = Instant.ofEpochMilli(deliveryDto.getEndTime());
    Instant currentDeliveryStartTime = Instant.ofEpochMilli(deliveryDto.getStartTime());

    List<Delivery> conflictedDeliveryList = null;
    if(deliveryMan == null)
      throw new DeliveryManNotRegisteredException("Delivery data should only be accepted from the registered delivery man");
    else if(customer == null)
      throw new CustomerNotRegisteredException("Delivery data should only be accepted from the registered customer");
    else if(deliveryDto.getStartTime() >= deliveryDto.getEndTime())
      throw new DateConfusionException("Start time must be less than end time");

    conflictedDeliveryList = deliveryRepository.findConflictedTimeInvervalsForDelivery(deliveryMan.getId(),
                                                                                      currentDeliveryStartTime,
                                                                                      currentDeliveryEndTime);
    if(conflictedDeliveryList.size() > 0)
      throw new TimeIntervalConflictedException("The delivery man is not allowed to deliver multiple orders at the same time");
    else {
      modelMapper.addConverter(deliveryMapperConverter);
      Delivery delivery = modelMapper.map(deliveryDto, Delivery.class);
      delivery.setDeliveryMan(deliveryMan);
      delivery.setCustomer(customer);
      return deliveryRepository.save(delivery);
    }
  }

  @Override
  public Delivery findById(Long deliveryId) {
    Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
    return optionalDelivery.isPresent() ? optionalDelivery.get() : null;
  }

  @Override
  public List<DeliveryManDto> getTop3Leaderboard(Long start_time, Long end_time) {
    if(start_time >= end_time)
      throw new DateConfusionException("Start time must be less than end time");
    else {
      modelMapper.addConverter(deliveryManMapperConverter);
      Instant start = Instant.ofEpochMilli(start_time);
      Instant end = Instant.ofEpochMilli(end_time);

      List<DeliveryManDto> deliveryManDtoResult = new ArrayList<DeliveryManDto>();
      List<Delivery> deliveries = deliveryRepository.findTop3ByStartTimeGreaterThanEqualAndEndTimeLessThanEqualOrderByPriceDesc(start, end);
      for (Delivery delivery: deliveries) {
        DeliveryManDto deliveryManDto = modelMapper.map(delivery, DeliveryManDto.class);
        deliveryManDtoResult.add(deliveryManDto);
      }
      return deliveryManDtoResult;
    }
  }

  @Override
  public List<Delivery> findDeliveryDurationGreaterThanMinutes(Integer minute) {
    return deliveryRepository.findDeliveryDurationGreaterThanMinutes(minute);
  }

}
