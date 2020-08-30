package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.infrastructure.time;


import com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.time.TimeProvider;

import java.time.*;

public class TestClockTimeProvider implements TimeProvider {

    private Clock clock;

    private TestClockTimeProvider(Clock clock) {
        this.clock = clock;
    }

    public static TestClockTimeProvider withFixedTime(LocalTime localTime) {
        return withFixedTime(localTime, ZoneId.systemDefault());
    }

    public static TestClockTimeProvider withFixedTime(LocalTime localTime, ZoneId zoneId) {
        return new TestClockTimeProvider(Clock.fixed(currentInstantWithTime(localTime), zoneId));
    }

    private static Instant currentInstantWithTime(LocalTime localTime) {
        return ZonedDateTime.of(LocalDate.now().atTime(localTime).withSecond(0), ZoneId.systemDefault()).toInstant();
    }

    @Override
    public LocalTime getCurrentLocalTime() {
        return LocalTime.now(clock);
    }

    public void setCurrentTimeTo(LocalTime localTime) {
        clock = Clock.fixed(currentInstantWithTime(localTime), clock.getZone());
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
