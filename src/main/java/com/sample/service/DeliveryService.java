package com.sample.service;

import com.sample.dto.DeliveryDto;
import com.sample.model.Delivery;
import com.sample.dto.DeliveryManDto;
import java.util.List;

public interface DeliveryService {

  public Delivery save(DeliveryDto deliveryDto);

  public Delivery findById(Long deliveryId);

  public List<DeliveryManDto> getTop3Leaderboard(Long start_time, Long end_time);

  List<Delivery> findDeliveryDurationGreaterThanMinutes(Integer minute);
}
