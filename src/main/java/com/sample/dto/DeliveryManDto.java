package com.sample.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryManDto {

    private Long id;
    private String name;
    private String email;
    private String registrationNumber;
    private Double commission;
}
