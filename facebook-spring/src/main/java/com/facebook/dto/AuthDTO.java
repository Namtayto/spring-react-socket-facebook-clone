package com.facebook.dto;

import com.facebook.annotation.ValidPassword;
import jakarta.validation.constraints.Email;

public class AuthDTO {
    public record LoginRequest(String username, String password) {
    }

    public record Response(String message, String token) {
    }

    public record RegisterRequest(String firstName, String lastName, String username,
                                  @Email String email, @ValidPassword String password) {
    }
}
