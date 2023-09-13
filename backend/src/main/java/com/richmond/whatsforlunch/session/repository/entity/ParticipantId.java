package com.richmond.whatsforlunch.session.repository.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * Composite ID for a participant.
 */
@Embeddable
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ParticipantId implements Serializable {
    private long sessionId;
    private long userId;
}
