package com.richmond.whatsforlunch.session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to manage participant.
 */
@RestController
@RequestMapping("/api/v1/sessions/{sessionId}/participants")
@RequiredArgsConstructor
public class ParticipantController {
}
