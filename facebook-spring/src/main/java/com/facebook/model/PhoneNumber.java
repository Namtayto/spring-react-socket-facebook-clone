package com.facebook.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "phones")
public class PhoneNumber {
    @Id
    private Long id;

    @NotBlank
    private String value;

    @NotBlank
    private String locale;

}
