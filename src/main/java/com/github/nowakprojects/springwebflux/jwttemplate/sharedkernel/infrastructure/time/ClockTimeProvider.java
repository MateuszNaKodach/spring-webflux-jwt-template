package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.infrastructure.time;


import com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.time.TimeProvider;

import java.time.*;

class ClockTimeProvider implements TimeProvider {

    private final Clock clock;

    ClockTimeProvider(Clock clock) {
        this.clock = clock;
    }

    @Override
    public LocalTime getCurrentLocalTime() {
        return LocalTime.now(clock);
    }

    @Override
    public LocalDate getCurrentLocalDate() {
        return LocalDateTime.ofInstant(clock.instant(), clock.getZone()).toLocalDate();
    }

    @Override
    public Instant getCurrentInstant() {
        return clock.instant();
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }
}

