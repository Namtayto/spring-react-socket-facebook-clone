package com.facebook.security.jwt;

import org.springframework.beans.factory.annotation.Value;

public class JwtConstant {

    @Value("${jwt.secret.key}")
    public static String SECRET_KEY;

    @Value("${jwt.secret.expirationMs}")
    public static int EXPIRATION;
    public static final String JWT_HEADER = "Authorization";
}
