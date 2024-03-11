package com.facebook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "phones")
@Embeddable
public class PhoneNumber {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String phoneCode;

}