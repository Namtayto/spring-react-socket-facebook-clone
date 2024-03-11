package com.facebook.model;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    private String value;

    @NotBlank
    private String locale;

    @OneToOne(mappedBy = "phoneNumber")
    private User user;

}