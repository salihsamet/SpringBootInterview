package com.sample.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryDto{

   @NotNull
   @Min(0)
   private Long startTime;

   @NotNull
   @Min(0)
   private Long endTime;

   @NotNull
   @Min(0)
   private Long distance;

   @NotNull
   @Min(0)
   private Long price;

   @NotNull
   private Long delivery_man_id;

   @NotNull
   private Long customer_id;

   @Override
   public String toString() {
      return "DeliveryDto{" +
              "startTime=" + startTime +
              ", endTime=" + endTime +
              ", distance=" + distance +
              ", price=" + price +
              ", delivery_man_id=" + delivery_man_id +
              ", customer_id=" + customer_id +
              '}';
   }
}
