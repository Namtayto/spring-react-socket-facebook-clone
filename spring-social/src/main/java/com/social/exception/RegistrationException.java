package com.social.exception;

public class RegistrationException extends Exception {
    private String usernameError;
    private String emailError;

    public RegistrationException(String usernameError, String emailError) {
        super("Registration failed");
        this.usernameError = usernameError;
        this.emailError = emailError;
    }

    public String getUsernameError() {
        return usernameError;
    }

    public String getEmailError() {
        return emailError;
    }
}
