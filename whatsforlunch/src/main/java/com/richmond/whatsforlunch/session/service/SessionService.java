package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.repository.SessionRepository;
import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import com.richmond.whatsforlunch.session.service.dto.Session;
import com.richmond.whatsforlunch.users.repository.UserRepository;
import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Service to manage session.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public Session createNewSession(final LocalDate date, final long ownerId, final Collection<Long> participantIds) {

        final List<Long> userIds = new ArrayList<>(participantIds);
        userIds.add(ownerId);
        final List<UserEntity> users = userRepository.findAllById(userIds);

        final UserEntity owner = users.stream().filter(u -> u.getId() == ownerId).findAny().orElse(null);

        SessionEntity session = SessionEntity.builder().date(date).owner(owner).build();
        sessionRepository.saveAndFlush(session);
        return mapToBean(session);
    }

    private Session mapToBean(final SessionEntity entity) {
        return new Session(entity.getId(), entity.getDate(), entity.getOwner().getId(), new ArrayList<>(), entity.getVersion());
    }
}
