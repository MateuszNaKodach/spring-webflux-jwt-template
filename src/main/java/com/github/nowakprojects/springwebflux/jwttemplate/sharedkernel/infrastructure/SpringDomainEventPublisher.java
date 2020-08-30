package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.infrastructure;

import com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.application.DomainEventPublisher;
import com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.event.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    SpringDomainEventPublisher(ApplicationEventPublisher publisher) {
        applicationEventPublisher = publisher;
    }

    @Override
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
