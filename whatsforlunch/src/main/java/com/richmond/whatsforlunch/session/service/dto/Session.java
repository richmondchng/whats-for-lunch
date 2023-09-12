package com.richmond.whatsforlunch.session.service.dto;

import java.time.LocalDate;
import java.util.Collection;

public record Session(long id, LocalDate date, long owner, Collection<Long> participants) {

}
