package com.sample.dto;

import com.sample.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class PersonDto {


    private String name;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private UserRole userRole;

    @Override
    public String toString() {
        return "PersonDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}
