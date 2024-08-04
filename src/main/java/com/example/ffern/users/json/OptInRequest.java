package com.example.ffern.users.json;

import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record OptInRequest(
        @NotNull(message = "Please provide a cohort") String cohort,
        @NotNull(message = "Please provide a region") String region
) {}
