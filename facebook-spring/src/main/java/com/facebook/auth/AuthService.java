package com.facebook.auth;

import com.facebook.dto.AuthDTO;
import com.facebook.model.User;
import com.facebook.repository.UserRepository;
import com.facebook.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String login(AuthDTO.LoginRequest userLogin) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogin.username(), userLogin.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Token requested for user :{}", authentication.getAuthorities());

        return generateToken(authentication);
    }


    public boolean signup(@Valid AuthDTO.RegisterRequest userSignup) throws MessagingException, UnsupportedEncodingException {
        User registerUser = User.builder()
                .firstName(userSignup.firstName()).lastName(userSignup.lastName())
                .username(userSignup.username()).email(userSignup.email())
                .password(passwordEncoder.encode(userSignup.password()))
                .verificationCode(RandomString.make(64))
                .build();

        if (userRepository.findByUsername(registerUser.getUsername()).isPresent()) {
            return false;
        }

        if (userRepository.findByEmail(registerUser.getEmail()).isPresent()) {
            return false;
        }

        userRepository.save(registerUser);
        mailService.sendVerificationEmail(registerUser, registerUser.getEmail());
        return false;
    }

}