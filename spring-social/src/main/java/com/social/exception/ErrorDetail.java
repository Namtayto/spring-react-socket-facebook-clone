package com.social.exception;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ErrorDetail {
    private String error;
    private String message;
    private LocalDateTime timeStamp;
}
