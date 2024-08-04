package com.example.ffern.users.matchers;

import com.example.ffern.users.json.UserResponse;
import com.example.ffern.waitlist.json.WaitlistResponse;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class UserResponseMatchers {

    @SafeVarargs
    public static Matcher<UserResponse> userResponse(Matcher<UserResponse>... matchers) {
        return allOf(matchers);
    }

    public static Matcher<UserResponse> id(Long expected) {
        return new FeatureMatcher<>(is(expected), "id", "but id") {
            @Override
            protected Long featureValueOf(UserResponse actual) {
                return actual.id();
            }
        };
    }

    public static Matcher<UserResponse> firstName(String expected) {
        return new FeatureMatcher<>(is(expected), "firstName", "but firstName") {
            @Override
            protected String featureValueOf(UserResponse actual) {
                return actual.firstName();
            }
        };
    }

    public static Matcher<UserResponse> lastName(String expected) {
        return new FeatureMatcher<>(is(expected), "lastName", "but lastName") {
            @Override
            protected String featureValueOf(UserResponse actual) {
                return actual.lastName();
            }
        };
    }

    public static Matcher<UserResponse> email(String expected) {
        return new FeatureMatcher<>(is(expected), "email", "but email") {
            @Override
            protected String featureValueOf(UserResponse actual) {
                return actual.email();
            }
        };
    }

    public static Matcher<UserResponse> phoneNumber(String expected) {
        return new FeatureMatcher<>(is(expected), "phoneNumber", "but phoneNumber") {
            @Override
            protected String featureValueOf(UserResponse actual) {
                return actual.phoneNumber();
            }
        };
    }

    public static Matcher<UserResponse> optIn(Boolean expected) {
        return new FeatureMatcher<>(is(expected), "optIn", "but optIn") {
            @Override
            protected Boolean featureValueOf(UserResponse actual) {
                return actual.optIn();
            }
        };
    }

    @SafeVarargs
    public static Matcher<UserResponse> waitlist(Matcher<WaitlistResponse>... matchers) {
        return new FeatureMatcher<>(allOf(matchers), "waitlist", "but waitlist") {
            @Override
            protected WaitlistResponse featureValueOf(UserResponse actual) {
                return actual.waitlist();
            }
        };
    }
}
