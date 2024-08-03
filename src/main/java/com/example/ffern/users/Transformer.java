package com.example.ffern.users;

import com.example.ffern.users.json.UserRequest;
import com.example.ffern.users.json.UserResponse;
import com.example.ffern.users.repository.UserEntity;
import com.example.ffern.waitlist.WaitlistEntity;
import com.example.ffern.waitlist.json.WaitlistResponse;

class Transformer {

    public static UserEntity toEntity(UserRequest request) {
        return UserEntity.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .optIn(true)
                .build();
    }

    public static UserResponse toResponse(UserEntity entity) {
        return UserResponse.builder()
                           .id(entity.getId())
                           .firstName(entity.getFirstName())
                           .lastName(entity.getLastName())
                           .email(entity.getEmail())
                           .phoneNumber(entity.getPhoneNumber())
                           .optIn(entity.getOptIn())
                            .waitlist(entity.getWaitlist() == null ? null : toResponse(entity.getWaitlist()))
                           .createdAt(entity.getCreatedAt().getTime())
                           .updatedAt(entity.getUpdatedAt().getTime())
                           .build();
    }

    public static WaitlistResponse toResponse(WaitlistEntity entity) {
        return WaitlistResponse.builder()
                               .id(entity.getId())
                               .userId(entity.getWaitlistUser().getId())
                               .cohort(entity.getCohort())
                               .region(entity.getRegion())
                               .createdAt(entity.getCreatedAt().getTime())
                               .build();
    }
}
