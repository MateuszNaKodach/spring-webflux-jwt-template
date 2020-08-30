package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.application;

import com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.event.DomainEvent;

import java.util.Collection;

public interface DomainEventPublisher {

    <T extends DomainEvent> void publish(T event);

    default <T extends DomainEvent> void publishAll(Collection<T> events) {
        events.forEach(this::publish);
    }
}

