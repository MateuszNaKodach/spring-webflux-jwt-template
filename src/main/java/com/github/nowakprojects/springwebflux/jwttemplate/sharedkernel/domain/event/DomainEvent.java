package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.event;

import java.time.Instant;

public interface DomainEvent {

    DomainEventId eventId();

    Instant eventOccurredAt();
}
