package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.infrastructure.time;

import com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.time.TimeProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@AutoConfigureAfter(value = CurrentTimeProperties.class)
class TimeConfiguration {

    private final CurrentTimeProperties timeProperties;

    public TimeConfiguration(CurrentTimeProperties timeProperties) {
        this.timeProperties = timeProperties;
    }

    @Bean
    Clock getClock() {
        return timeProperties.getClock();
    }

    @Bean
    TimeProvider getClockTimeProvider() {
        return new ClockTimeProvider(getClock());
    }
}

