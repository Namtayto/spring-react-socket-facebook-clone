package com.social.auth;

import com.social.dto.AuthDTO;
import com.social.exception.RegistrationException;
import com.social.model.user.User;
import com.social.repository.UserRepository;
import com.social.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
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
import java.util.Optional;
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

        Optional<User> user = userRepository.findByUsername(authentication.getName());

        if (!user.get().isEnabled()) {
            throw new DisabledException("User account is not verified");
        }

        log.info("Token requested for user :{}", authentication.getAuthorities());
        return generateToken(authentication);
    }


    public boolean signup(@Valid AuthDTO.RegisterRequest userSignup) throws MessagingException, UnsupportedEncodingException, RegistrationException {
        User registerUser = User.builder()
                .firstName(userSignup.firstName()).lastName(userSignup.lastName())
                .username(userSignup.username()).email(userSignup.email())
                .password(passwordEncoder.encode(userSignup.password()))
                .verificationCode(RandomString.make(64))
                .build();

        boolean usernameExists = userRepository.findByUsername(userSignup.username()).isPresent();
        boolean emailExists = userRepository.findByEmail(userSignup.email()).isPresent();

        if (usernameExists && emailExists) {
            throw new RegistrationException("Username is already taken", "Email is already registered");
        } else if (usernameExists) {
            throw new RegistrationException("Username is already taken", null);
        } else if (emailExists) {
            throw new RegistrationException(null, "Email is already registered");
        }

        userRepository.save(registerUser);
        mailService.sendVerificationEmail(registerUser, registerUser.getEmail());
        return true;
    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null) {
            return false;
        }

        user.setVerificationCode(null);
        user.setEnabled(true);
        userRepository.save(user);

        return true;
    }

}