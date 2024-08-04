package com.example.ffern.waitlist.json;

import lombok.Builder;

@Builder
public record WaitlistResponse(
        long id,
        long userId,
        String cohort,
        String region,
        long createdAt
) {}
