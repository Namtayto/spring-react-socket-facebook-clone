package com.facebook.dto;

public class AuthDTO {
    public record LoginRequest(String username, String password) {
    }

    public record Response(String message, String token) {
    }

    public record RegisterRequest(String firstName, String lastName, String username,
                                  String email, String password) {
    }
}
