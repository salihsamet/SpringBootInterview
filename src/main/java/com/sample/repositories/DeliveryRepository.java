package com.sample.repositories;

import com.sample.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import java.time.Instant;
import java.util.List;

@RestResource(exported = false)
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findTop3ByStartTimeGreaterThanEqualAndEndTimeLessThanEqualOrderByPriceDesc(Instant startTime,
                                                                                                 Instant endTime);

    @Query(value = "SELECT * " +
            "FROM delivery " +
            "WHERE delivery_man_id = :deliveryManId " +
            "AND ((end_time >= :endTime AND start_time < :endTime) " +
            "OR (start_time <= :startTime AND end_time > :startTime) " +
            "OR (start_time > :startTime AND end_time < :endTime))", nativeQuery = true)
    List<Delivery> findConflictedTimeInvervalsForDelivery(@Param("deliveryManId") Long deliveryManId,
                                 @Param("startTime") Instant startTime,
                                 @Param("endTime") Instant endTime);


    @Query(value = "SELECT * " +
            "FROM delivery " +
            "WHERE TIMESTAMPDIFF(MINUTE, start_time, end_time) > :minute", nativeQuery = true)
    List<Delivery> findDeliveryDurationGreaterThanMinutes(@Param("minute") Integer minute);

}
