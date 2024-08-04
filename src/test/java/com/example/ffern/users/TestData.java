package com.example.ffern.users;

import com.example.ffern.users.json.OptInRequest;
import com.example.ffern.users.json.UserAnalyticsRequest;
import com.example.ffern.users.json.UserRequest;
import com.example.ffern.users.repository.UserAnalyticsEntity;
import com.example.ffern.users.repository.UserEntity;
import com.example.ffern.waitlist.WaitlistEntity;

import java.sql.Timestamp;
import java.util.Map;

public class TestData {

    public static final String FIRST_NAME = "John";
    public static final String FIRST_NAME_2 = "Jane";
    public static final String LAST_NAME = "Doe";
    public static final String LAST_NAME_2 = "Smith";
    public static final String EMAIL = "test@ffern.com";
    public static final String EMAIL_2 = "test-2@ffern.com";
    public static final String PHONE_NUMBER = "123456789";
    public static final String PHONE_NUMBER_2 = "987654321";
    public static final String COHORT = "cohort";
    public static final String COHORT_2 = "cohort-2";
    public static final String UK = "UK";
    public static final String US = "US";
    public static final String OPERATOR = "operator";

    public static final UserRequest USER_REQUEST = UserRequest.builder()
                                                              .firstName(FIRST_NAME)
                                                              .lastName(LAST_NAME)
                                                              .email(EMAIL)
                                                              .phoneNumber(PHONE_NUMBER)
                                                              .cohort(COHORT)
                                                              .region(UK)
                                                              .build();

    public static final UserRequest USER_REQUEST_2 = UserRequest.builder()
                                                                .firstName(FIRST_NAME_2)
                                                                .lastName(LAST_NAME_2)
                                                                .email(EMAIL_2)
                                                                .phoneNumber(PHONE_NUMBER_2)
                                                                .cohort(COHORT_2)
                                                                .region(US)
                                                                .build();

    public static final OptInRequest OPT_IN_REQUEST = OptInRequest.builder()
                                                                  .cohort(COHORT)
                                                                  .region(UK)
                                                                  .build();

    public static final UserAnalyticsRequest USER_ANALYTICS_REQUEST = UserAnalyticsRequest.builder()
                                                                                          .data(Map.of(
                                                                                                  "key1", "value1",
                                                                                                  "key2", "value2",
                                                                                                  "key3", "value3"
                                                                                          ))
                                                                                          .build();

}
