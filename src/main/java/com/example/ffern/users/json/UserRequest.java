package com.example.ffern.users.json;

import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record UserRequest(
        @NotNull(message = "Please provide a first name") String firstName,
        @NotNull(message = "Please provide a last name") String lastName,
        @NotNull(message = "Please provide an email") String email,
        @NotNull(message = "Please provide a phone number") String phoneNumber,
        @NotNull(message = "Please provide a cohort") String cohort,
        @NotNull(message = "Please provide a region") String region
) {}
