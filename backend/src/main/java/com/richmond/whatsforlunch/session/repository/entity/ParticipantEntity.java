package com.richmond.whatsforlunch.session.repository.entity;

import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data entity representing state of 1 participant in 1 session.
 */
@Entity(name = "session_participants")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantEntity {

    @EmbeddedId
    private ParticipantId id;

    @ManyToOne
    @MapsId("userId")
    private UserEntity user;

    @ManyToOne
    @MapsId("sessionId")
    private SessionEntity session;

    @Enumerated(EnumType.STRING)
    private ParticipantStatus status;

    @Version
    private long version;
}
