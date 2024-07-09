package com.social.controller;


import com.social.auth.AuthService;
import com.social.dto.AuthDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Operation(summary = "Check the credentials of a user and let them in if they are correct")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Log In successful", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "400", description = "invalid credentials", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest userLogin) throws IllegalAccessException {
        String token = authService.login(userLogin);

        AuthDTO.Response response = new AuthDTO.Response("User logged in successfully", token);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Redirect the user to the signUp view")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AuthDTO.RegisterRequest request)
            throws MessagingException, IOException {
        boolean hasBeenRegistered = authService.signup(request);

        if (hasBeenRegistered) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();

    }

}