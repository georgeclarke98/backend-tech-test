package com.example.ffern.users;

import com.example.ffern.users.json.UserAnalyticsResponse;
import com.example.ffern.users.json.UserResponse;
import com.example.ffern.users.matchers.UserAnalyticsResponseMatchers;
import com.example.ffern.users.matchers.UserResponseMatchers;
import com.example.ffern.users.matchers.WaitlistResponseMatcher;
import com.example.ffern.users.repository.UserAnalyticsEntity;
import com.example.ffern.users.repository.UserAnalyticsRepository;
import com.example.ffern.users.repository.UserEntity;
import com.example.ffern.users.repository.UserRepository;
import com.example.ffern.waitlist.WaitlistEntity;
import com.example.ffern.waitlist.WaitlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.Optional;

import static com.example.ffern.users.TestData.*;
import static com.example.ffern.users.matchers.UserAnalyticsResponseMatchers.data;
import static com.example.ffern.users.matchers.UserAnalyticsResponseMatchers.userAnalyticsResponse;
import static com.example.ffern.users.matchers.UserResponseMatchers.*;
import static com.example.ffern.users.matchers.WaitlistResponseMatcher.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WaitlistRepository waitlistRepository;

    @Mock
    private UserAnalyticsRepository userAnalyticsRepository;

    private UserService userService;

    private UserEntity USER_ENTITY;
    private WaitlistEntity WAITLIST_ENTITY;

    @BeforeEach
    public void setUp() {

        userService = new UserService(userRepository, waitlistRepository, userAnalyticsRepository);

        USER_ENTITY = UserEntity.builder()
                                .id(1L)
                                .firstName(FIRST_NAME)
                                .lastName(LAST_NAME)
                                .email(EMAIL)
                                .phoneNumber(PHONE_NUMBER)
                                .optIn(true)
                                .createdAt(new Timestamp(System.currentTimeMillis()))
                                .updatedAt(new Timestamp(System.currentTimeMillis()))
                                .build();

        WAITLIST_ENTITY = WaitlistEntity.builder()
                                        .id(1L)
                                        .waitlistUser(USER_ENTITY)
                                        .cohort(COHORT)
                                        .region(UK)
                                        .createdAt(new Timestamp(System.currentTimeMillis()))
                                        .build();

        USER_ENTITY.setWaitlist(WAITLIST_ENTITY);
    }

    @Test
    public void createUser() {

        when(userRepository.save(any())).thenReturn(USER_ENTITY);
        when(waitlistRepository.save(any())).thenReturn(WAITLIST_ENTITY);

        UserResponse result = userService.createUser(USER_REQUEST);

        verify(userRepository).save(UserEntity.builder()
                                              .firstName(FIRST_NAME)
                                              .lastName(LAST_NAME)
                                              .email(EMAIL)
                                              .phoneNumber(PHONE_NUMBER)
                                              .optIn(true)
                                              .build());

        verify(waitlistRepository).save(WaitlistEntity.builder()
                                                      .waitlistUser(USER_ENTITY)
                                                      .cohort(COHORT)
                                                      .region(UK)
                                                      .build());

        assertThat(result, userResponse(
                UserResponseMatchers.id(1L),
                firstName(FIRST_NAME),
                lastName(LAST_NAME),
                email(EMAIL),
                phoneNumber(PHONE_NUMBER),
                optIn(true),
                waitlist(waitlistResponse(
                        WaitlistResponseMatcher.id(1L),
                        userId(1L),
                        cohort(COHORT),
                        region(UK)
                ))
        ));
    }

    @Test
    public void getUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(USER_ENTITY));

        UserResponse result = userService.getUser(1L);

        assertThat(result, userResponse(
                UserResponseMatchers.id(1L),
                firstName(FIRST_NAME),
                lastName(LAST_NAME),
                email(EMAIL),
                phoneNumber(PHONE_NUMBER),
                optIn(true),
                waitlist(waitlistResponse(
                        WaitlistResponseMatcher.id(1L),
                        userId(1L),
                        cohort(COHORT),
                        region(UK)
                ))
        ));

    }

    @Test
    public void getUser_notFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.getUser(1L));
    }

    @Test
    public void updateUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(USER_ENTITY));
        when(userRepository.save(any())).thenReturn(USER_ENTITY);

        userService.updateUser(1L, USER_REQUEST_2);

        verify(userRepository).save(USER_ENTITY.toBuilder()
                                               .firstName(FIRST_NAME_2)
                                               .lastName(LAST_NAME_2)
                                               .email(EMAIL_2)
                                               .phoneNumber(PHONE_NUMBER_2)
                                               .waitlist(WAITLIST_ENTITY.toBuilder()
                                                                        .cohort(COHORT_2)
                                                                        .region(US)
                                                                        .build())
                                               .build());
    }

    @Test
    public void deleteUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(USER_ENTITY));

        userService.deleteUser(1L);

        verify(userRepository).delete(USER_ENTITY);
    }

    @Test
    public void optInUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(USER_ENTITY.toBuilder()
                                                                            .optIn(false)
                                                                            .waitlist(null)
                                                                            .build()));
        when(userRepository.save(any())).thenReturn(USER_ENTITY);
        when(waitlistRepository.save(any())).thenReturn(WAITLIST_ENTITY);

        userService.optIn(1L, OPT_IN_REQUEST);

        verify(userRepository).save(USER_ENTITY.toBuilder()
                                               .optIn(true)
                                               .waitlist(WAITLIST_ENTITY)
                                               .build());

        verify(waitlistRepository).save(WaitlistEntity.builder()
                                                      .waitlistUser(USER_ENTITY.toBuilder().optIn(true).build())
                                                      .cohort(COHORT)
                                                      .region(UK)
                                                      .build());
    }

    @Test
    public void optOutUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(USER_ENTITY));
        when(userRepository.save(any())).thenReturn(USER_ENTITY);

        userService.optOut(1L);

        verify(userRepository).save(USER_ENTITY.toBuilder()
                                               .optIn(false)
                                               .waitlist(null)
                                               .build());

        verify(waitlistRepository).delete(WAITLIST_ENTITY);
    }

    @Test
    public void saveAnalytics() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(USER_ENTITY));
        when(userAnalyticsRepository.save(any())).thenReturn(UserAnalyticsEntity.builder()
                                                                                .id(1L)
                                                                                .analyticsUser(USER_ENTITY)
                                                                                .data(USER_ANALYTICS_REQUEST.data())
                                                                                .createdAt(new Timestamp(System.currentTimeMillis()))
                                                                                .build());

        UserAnalyticsResponse response = userService.saveAnalytics(1L, USER_ANALYTICS_REQUEST);

        verify(userAnalyticsRepository).save(UserAnalyticsEntity.builder()
                                                                .analyticsUser(USER_ENTITY)
                                                                .data(USER_ANALYTICS_REQUEST.data())
                                                                .build());

        assertThat(response, userAnalyticsResponse(
                           UserAnalyticsResponseMatchers.id(1L),
                           UserAnalyticsResponseMatchers.userId(1L),
                           data(
                                   aMapWithSize(3),
                                   hasEntry("key1", "value1"),
                                   hasEntry("key2", "value2"),
                                   hasEntry("key3", "value3")
                           )
                   )
        );
    }
}
