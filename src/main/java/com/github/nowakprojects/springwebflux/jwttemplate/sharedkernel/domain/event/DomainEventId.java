package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.event;

import java.util.Objects;
import java.util.UUID;

public class DomainEventId {

    private final String raw;

    private DomainEventId(String raw) {
        this.raw = raw;
    }

    public static DomainEventId generate() {
        return new DomainEventId(UUID.randomUUID().toString());
    }

    static DomainEventId fromRaw(String raw) {
        Objects.requireNonNull(raw);
        return new DomainEventId(raw);
    }

    @Override
    public String toString() {
        return this.raw;
    }

    String raw() {
        return raw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainEventId that = (DomainEventId) o;
        return raw.equals(that.raw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raw);
    }
}
