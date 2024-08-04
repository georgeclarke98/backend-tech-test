package com.example.ffern.users;

import com.example.ffern.users.json.*;
import com.example.ffern.users.repository.UserAnalyticsEntity;
import com.example.ffern.users.repository.UserAnalyticsRepository;
import com.example.ffern.users.repository.UserEntity;
import com.example.ffern.users.repository.UserRepository;
import com.example.ffern.waitlist.WaitlistEntity;
import com.example.ffern.waitlist.WaitlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.example.ffern.users.Transformer.toEntity;
import static com.example.ffern.users.Transformer.toResponse;
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WaitlistRepository waitlistRepository;
    private final UserAnalyticsRepository userAnalyticsRepository;

    public UserResponse createUser(UserRequest body) {

        log.info("Creating user with email: {}, first name: {}, last name: {}, phone number: {}",
                 body.email(), body.firstName(), body.lastName(), body.phoneNumber()
        );

        UserEntity user = userRepository.save(toEntity(body));

        log.info("Adding user with id: {} to the waiting list", user.getId());

        WaitlistEntity waitlistEntity = waitlistRepository.save(WaitlistEntity.builder()
                                                                              .waitlistUser(user)
                                                                              .cohort(body.cohort())
                                                                              .region(body.region())
                                                                              .build());

        return toResponse(user.toBuilder().waitlist(waitlistEntity).build());
    }

    public UserResponse getUser(Long id) {

        log.info("Getting user with id: {}", id);

        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND, format("User with Id %s not found", id))
        );

        return toResponse(user);
    }

    public UserResponse updateUser(Long id, UserRequest body) {

        log.info("Updating user with id: {}", id);

        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND, format("User with Id %s not found", id))
        );

        user.setFirstName(body.firstName());
        user.setLastName(body.lastName());
        user.setEmail(body.email());
        user.setPhoneNumber(body.phoneNumber());
        user.getWaitlist().setCohort(body.cohort());
        user.getWaitlist().setRegion(body.region());

        return toResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {

        log.info("Deleting user with id: {}", id);

        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND, format("User with Id %s not found", id))
        );

        userRepository.delete(user);
    }

    public UserResponse optIn(Long id, OptInRequest body) {

        log.info("Opting in user with id: {}", id);

        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND, format("User with Id %s not found", id))
        );

        user.setOptIn(true);

        WaitlistEntity waitlistEntity = waitlistRepository.save(WaitlistEntity.builder()
                                                                              .waitlistUser(user)
                                                                              .cohort(body.cohort())
                                                                              .region(body.region())
                                                                              .build());

        user.setWaitlist(waitlistEntity);

        return toResponse(userRepository.save(user));
    }

    public UserResponse optOut(Long id) {

        log.info("Opting out user with id: {}", id);

        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND, format("User with Id %s not found", id))
        );

        WaitlistEntity waitlist = user.getWaitlist();

        user.setOptIn(false);
        user.setWaitlist(null);

        waitlistRepository.delete(waitlist);

        return toResponse(userRepository.save(user));
    }

    public UserAnalyticsResponse saveAnalytics(Long id, UserAnalyticsRequest body) {

        log.info("Saving analytics for user with id: {}", id);

        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND, format("User with Id %s not found", id))
        );

        UserAnalyticsEntity entity = userAnalyticsRepository.save(UserAnalyticsEntity.builder()
                                                                                     .analyticsUser(user)
                                                                                     .data(body.data())
                                                                                     .build());

        return UserAnalyticsResponse.builder()
                                    .id(entity.getId())
                                    .userId(entity.getAnalyticsUser().getId())
                                    .data(entity.getData())
                                    .createdAt(entity.getCreatedAt().getTime())
                                    .build();
    }
}
