package com.richmond.whatsforlunch.common.controller;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record StandardErrorResponse (
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path)
{ }
