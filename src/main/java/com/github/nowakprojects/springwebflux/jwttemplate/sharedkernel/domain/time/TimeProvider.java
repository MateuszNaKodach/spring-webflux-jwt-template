package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.time;

import java.time.*;

public interface TimeProvider {
    LocalTime getCurrentLocalTime();

    LocalDate getCurrentLocalDate();

    Instant getCurrentInstant();

    default LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.of(getCurrentLocalDate(), getCurrentLocalTime());
    }

    default LocalDateTime getStartOfCurrentDay() {
        return getCurrentLocalDate().atStartOfDay();
    }

    ZoneId getZone();

    default ZoneOffset getZoneOffset() {
        return getZone().getRules().getOffset(getCurrentInstant());
    }
}

