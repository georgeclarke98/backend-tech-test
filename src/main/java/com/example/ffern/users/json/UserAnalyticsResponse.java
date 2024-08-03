package com.example.ffern.users.json;

import lombok.Builder;

import java.util.Map;

@Builder
public record UserAnalyticsResponse(
        long id,
        long userId,
        Map<String, Object> data,
        long createdAt,
        String createdBy
) {}
