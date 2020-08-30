package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.valueobject;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Valid
@Value
@RequiredArgsConstructor
public class Place {

    @Valid
    @NotNull
    private Address address;

    @Valid
    @NotNull
    private GeoLocation geoLocation;

}
