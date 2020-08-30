package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.valueobject;

import lombok.Value;

@Value
public class QuestionStats {
    private final Long total;
    private final Long filled;
}
