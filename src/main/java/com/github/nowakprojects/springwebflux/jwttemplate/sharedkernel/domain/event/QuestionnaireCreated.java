package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.event;

import com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.valueobject.Place;
import lombok.Value;

import java.time.Instant;

@Value
public class QuestionnaireCreated implements DomainEvent {
    private final String questionnaireId;
    private final String questionnaireTemplateId;
    private final Long questionnaireTemplateVersion;
    private final Instant availabilityDateFrom;
    private final Instant availabilityDateTo;
    private final String company;
    private final Place place;
    private final String title;
    private final String description;
    private final String auditorId;
    private final Instant eventOccurredAt;
    private final String auditorUsername;

    @Override
    public DomainEventId eventId() {
        return DomainEventId.generate();
    }

    @Override
    public Instant eventOccurredAt() {
        return eventOccurredAt;
    }
}
