package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.domain.valueobject;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@RequiredArgsConstructor
public class Address {
    @NotBlank
    private final String country;

    @NotBlank
    private final String city;

    @NotBlank
    private final String street;

    @NotBlank
    private final String homeNumber;

    @NotBlank
    private final String postalCode;
}
