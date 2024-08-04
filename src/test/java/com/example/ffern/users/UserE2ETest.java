package com.example.ffern.users;

import com.example.ffern.users.json.UserAnalyticsResponse;
import com.example.ffern.users.json.UserResponse;
import com.example.ffern.users.matchers.UserAnalyticsResponseMatchers;
import com.example.ffern.users.matchers.UserResponseMatchers;
import com.example.ffern.users.repository.UserAnalyticsRepository;
import com.example.ffern.users.repository.UserRepository;
import com.example.ffern.waitlist.WaitlistRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.example.ffern.users.TestData.*;
import static com.example.ffern.users.matchers.UserAnalyticsResponseMatchers.data;
import static com.example.ffern.users.matchers.UserAnalyticsResponseMatchers.userAnalyticsResponse;
import static com.example.ffern.users.matchers.UserResponseMatchers.*;
import static com.example.ffern.users.matchers.WaitlistResponseMatcher.*;
import static com.example.ffern.users.matchers.WaitlistResponseMatcher.region;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserE2ETest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine");

    private static String baseUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WaitlistRepository waitlistRepository;

    @Autowired
    private UserAnalyticsRepository userAnalyticsRepository;

    @BeforeAll
    public static void setUp() {

        postgresContainer.start();
    }

    @AfterAll
    public static void tearDown() {

        postgresContainer.stop();
    }

    @BeforeEach
    public void init() {

        baseUrl = "http://localhost:" + port;
        userAnalyticsRepository.deleteAll();
        waitlistRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    public void createGetUpdateAndDeleteUser() {

        Long userId = testRestTemplate.withBasicAuth("admin", "password")
                                      .exchange(
                                              baseUrl + "/ffern/users",
                                              HttpMethod.POST,
                                              new HttpEntity<>(USER_REQUEST, new HttpHeaders()),
                                              UserResponse.class
                                      ).getBody().id();

        UserResponse result = testRestTemplate.withBasicAuth("admin", "password")
                                              .exchange(
                                                      baseUrl + "/ffern/users?userId=" + userId,
                                                      HttpMethod.GET,
                                                      new HttpEntity<>(new HttpHeaders()),
                                                      UserResponse.class
                                              ).getBody();

        assertThat(result, userResponse(
                UserResponseMatchers.id(userId),
                firstName(FIRST_NAME),
                lastName(LAST_NAME),
                email(EMAIL),
                phoneNumber(PHONE_NUMBER),
                optIn(true),
                waitlist(waitlistResponse(
                        userId(userId),
                        cohort(COHORT),
                        region(UK)
                ))
        ));

        UserResponse updatedResult = testRestTemplate.withBasicAuth("admin", "password")
                                                     .exchange(
                                                             baseUrl + "/ffern/users?userId=" + userId,
                                                             HttpMethod.PUT,
                                                             new HttpEntity<>(USER_REQUEST_2, new HttpHeaders()),
                                                             UserResponse.class
                                                     ).getBody();

        assertThat(updatedResult, userResponse(
                UserResponseMatchers.id(userId),
                firstName(FIRST_NAME_2),
                lastName(LAST_NAME_2),
                email(EMAIL_2),
                phoneNumber(PHONE_NUMBER_2),
                optIn(true),
                waitlist(waitlistResponse(
                        userId(userId),
                        cohort(COHORT_2),
                        region(US)
                ))
        ));

        testRestTemplate.withBasicAuth("admin", "password")
                       .exchange(
                               baseUrl + "/ffern/users?userId=" + userId,
                               HttpMethod.DELETE,
                               new HttpEntity<>(new HttpHeaders()),
                               String.class
                       );

        assertEquals(NOT_FOUND, testRestTemplate.withBasicAuth("admin", "password")
                                               .exchange(
                                                       baseUrl + "/ffern/users?userId=" + userId,
                                                       HttpMethod.GET,
                                                       new HttpEntity<>(new HttpHeaders()),
                                                       UserResponse.class
                                               ).getStatusCode());
    }

    @Test
    public void optOutAndOptInUser() {

        Long userId = testRestTemplate.withBasicAuth("admin", "password")
                                      .exchange(
                                              baseUrl + "/ffern/users",
                                              HttpMethod.POST,
                                              new HttpEntity<>(USER_REQUEST, new HttpHeaders()),
                                              UserResponse.class
                                      ).getBody().id();

        UserResponse optOutResult = testRestTemplate.withBasicAuth("admin", "password")
                .exchange(
                        baseUrl + "/ffern/users/opt-out?userId=" + userId,
                        HttpMethod.PUT,
                        new HttpEntity<>(USER_REQUEST, new HttpHeaders()),
                        UserResponse.class
                ).getBody();

        assertThat(optOutResult, userResponse(
                UserResponseMatchers.id(userId),
                firstName(FIRST_NAME),
                lastName(LAST_NAME),
                email(EMAIL),
                phoneNumber(PHONE_NUMBER),
                optIn(false)
        ));

        assertNull(optOutResult.waitlist());

        UserResponse optInResult = testRestTemplate.withBasicAuth("admin", "password")
                .exchange(
                        baseUrl + "/ffern/users/opt-in?userId=" + userId,
                        HttpMethod.PUT,
                        new HttpEntity<>(OPT_IN_REQUEST, new HttpHeaders()),
                        UserResponse.class
                ).getBody();

        assertThat(optInResult, userResponse(
                UserResponseMatchers.id(userId),
                firstName(FIRST_NAME),
                lastName(LAST_NAME),
                email(EMAIL),
                phoneNumber(PHONE_NUMBER),
                optIn(true),
                waitlist(waitlistResponse(
                        userId(userId),
                        cohort(COHORT),
                        region(UK)
                ))
        ));
    }

    @Test
    public void saveAnalytics() {

        Long userId = testRestTemplate.withBasicAuth("admin", "password")
                                      .exchange(
                                              baseUrl + "/ffern/users",
                                              HttpMethod.POST,
                                              new HttpEntity<>(USER_REQUEST, new HttpHeaders()),
                                              UserResponse.class
                                      ).getBody().id();

        UserAnalyticsResponse result = testRestTemplate.withBasicAuth("admin", "password")
                                                       .exchange(
                                                               baseUrl + "/ffern/users/analytics?userId=" + userId,
                                                               HttpMethod.POST,
                                                               new HttpEntity<>(USER_ANALYTICS_REQUEST, new HttpHeaders()),
                                                               UserAnalyticsResponse.class
                                                       ).getBody();

        assertThat(result, userAnalyticsResponse(
                           UserAnalyticsResponseMatchers.id(1L),
                           UserAnalyticsResponseMatchers.userId(userId),
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
