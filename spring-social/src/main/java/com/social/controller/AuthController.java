package com.social.controller;


import com.social.auth.AuthService;
import com.social.dto.AuthDTO;
import com.social.exception.RegistrationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Check the credentials of a user and let them in if they are correct")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Log In successful", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad credentials", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest userLogin) {
        String token = authService.login(userLogin);

        AuthDTO.Response response = new AuthDTO.Response("User logged in successfully", token);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Register for the new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AuthDTO.RegisterRequest request)
            throws MessagingException, IOException, RegistrationException {
        boolean hasBeenRegistered = authService.signup(request);

        if (hasBeenRegistered) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();

    }

    @Operation(summary = "Check the verification code to create an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account verified", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "400", description = "invalid Token", content = @Content)
    })
    @GetMapping("/verification")
    public ResponseEntity<Object> verifyUser(@Param("code") String code) {

        if (authService.verify(code)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

}