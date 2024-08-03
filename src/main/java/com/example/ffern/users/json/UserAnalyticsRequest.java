package com.example.ffern.users.json;

import lombok.Builder;

import java.util.Map;

@Builder
public record UserAnalyticsRequest(
        Map<String, Object> data,
        String createdBy
) {}
