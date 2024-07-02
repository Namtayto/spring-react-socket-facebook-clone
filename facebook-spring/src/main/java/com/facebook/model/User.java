package com.facebook.model;

import com.facebook.enums.EGender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users_fb", uniqueConstraints = {
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

    //@ValidPassword
    @NotBlank
    @JsonIgnore
    private String password;

    private String firstName;
    private String lastName;
    private String school;
    private String address;
    private String location;
    private String profilePicture;

//    @NotBlank
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private EGender gender;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PostComment> postComments = new ArrayList<>();

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> roles = new HashSet<>();

    public User(String userId, @NonNull String username, @NonNull String email, @NonNull String password) {
        this.id = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
