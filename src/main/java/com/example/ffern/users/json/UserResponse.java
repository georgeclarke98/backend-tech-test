package com.example.ffern.users.json;

import com.example.ffern.waitlist.json.WaitlistResponse;
import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        boolean optIn,
        WaitlistResponse waitlist,
        long createdAt,
        long updatedAt
) {}
