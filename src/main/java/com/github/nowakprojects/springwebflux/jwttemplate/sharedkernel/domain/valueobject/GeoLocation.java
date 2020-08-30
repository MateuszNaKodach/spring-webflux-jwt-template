package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.valueobject;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Valid
@Value
@RequiredArgsConstructor
public class GeoLocation {
    @DecimalMax(value = "90.0")
    @DecimalMin(value = "-90.0")
    private Double latitude;

    @DecimalMax(value = "180.0")
    @DecimalMin(value = "-180.0")
    private Double longitude;
}
