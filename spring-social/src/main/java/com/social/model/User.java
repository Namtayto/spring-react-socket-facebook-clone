package com.social.model;

import com.social.annotation.ValidPassword;
import com.social.enums.EGender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users_ntt", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "user_name")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private String id;

    @Column(name = "user_name", unique = true)
    @NotBlank
    private String username;

    @NotBlank
    @Column(name = "email", unique = true)
    @Size(max = 50)
    @Email
    private String email;

//    @Embedded
//    @ValidPhoneNumber
//    private PhoneNumber phoneNumber;

    @ValidPassword
    @NotBlank
    @JsonIgnore
    private String password;

    private String firstName;
    private String lastName;
    private String school;
    private String address;
    private String location;

    @Lob
    private Blob profilePicture;

    //@NotBlank
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private EGender gender;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled = false;

    public void setImages(String[] files) throws IOException {
        Resource image = new ClassPathResource(files[0]);
        this.profilePicture = BlobProxy.generateProxy(image.getInputStream(), image.contentLength());
    }


//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> roles = new HashSet<>();


}
