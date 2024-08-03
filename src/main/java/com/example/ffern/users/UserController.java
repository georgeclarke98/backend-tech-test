package com.example.ffern.users;

import com.example.ffern.users.json.UserAnalyticsRequest;
import com.example.ffern.users.json.UserAnalyticsResponse;
import com.example.ffern.users.json.UserRequest;
import com.example.ffern.users.json.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ffern/users")
public class UserController {

    private final UserService userService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest body) {

        log.info("POST request to create user with email: {}, first name: {}, last name: {}, phone number: {}",
                 body.email(), body.firstName(), body.lastName(), body.phoneNumber()
        );

        return ResponseEntity.status(CREATED)
                             .body(userService.createUser(body));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUser(@NotNull(message = "Please provide a user ID") @RequestParam
                                                Long userId) {

        log.info("GET request to get user with id: {}", userId);

        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUser(@NotNull(message = "Please provide a user ID") @RequestParam
                                                   Long userId,
                                                   @Valid @RequestBody UserRequest body) {

        log.info("PUT request to update user with id: {}", userId);

        return ResponseEntity.ok(userService.updateUser(userId, body));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@NotNull(message = "Please provide a user ID") @RequestParam Long userId) {

        log.info("DELETE request to delete user with id: {}", userId);

        userService.deleteUser(userId);

        return ResponseEntity.ok(format("User with id %s successfully deleted", userId));
    }

    @PutMapping(value = "/opt-out", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> optOutUser(@NotNull(message = "Please provide a user ID") @RequestParam
                                                   Long userId) {

        log.info("PUT request to opt out user with id: {}", userId);

        return ResponseEntity.ok(userService.optOut(userId));
    }

    @PutMapping(value = "/opt-in", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> optInUser(@NotNull(message = "Please provide a user ID") @RequestParam
                                                  Long userId) {

        log.info("PUT request to opt in user with id: {}", userId);

        return ResponseEntity.ok(userService.optIn(userId));
    }

    @PostMapping(value = "/analytics", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserAnalyticsResponse> saveAnalytics(@NotNull(message = "Please provide a user ID")
                                                               @RequestParam Long userId,
                                                               @Valid @RequestBody UserAnalyticsRequest body) {

        log.info("POST request to save analytics for user with id: {}", userId);

        return ResponseEntity.ok(userService.saveAnalytics(userId, body));
    }
}
