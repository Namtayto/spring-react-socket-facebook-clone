package com.social.model.user;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
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