package com.richmond.whatsforlunch.session.repository;

import com.richmond.whatsforlunch.session.repository.entity.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {
}
