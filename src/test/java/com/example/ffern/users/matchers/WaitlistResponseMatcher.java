package com.example.ffern.users.matchers;

import com.example.ffern.users.json.UserResponse;
import com.example.ffern.waitlist.json.WaitlistResponse;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class WaitlistResponseMatcher {

    @SafeVarargs
    public static Matcher<WaitlistResponse> waitlistResponse(Matcher<WaitlistResponse>... matchers) {
        return allOf(matchers);
    }

    public static Matcher<WaitlistResponse> id(Long expected) {
        return new FeatureMatcher<>(is(expected), "id", "but id") {
            @Override
            protected Long featureValueOf(WaitlistResponse actual) {
                return actual.id();
            }
        };
    }

    public static Matcher<WaitlistResponse> userId(Long expected) {
        return new FeatureMatcher<>(is(expected), "userId", "but userId") {
            @Override
            protected Long featureValueOf(WaitlistResponse actual) {
                return actual.userId();
            }
        };
    }

    public static Matcher<WaitlistResponse> cohort(String expected) {
        return new FeatureMatcher<>(is(expected), "cohort", "but cohort") {
            @Override
            protected String featureValueOf(WaitlistResponse actual) {
                return actual.cohort();
            }
        };
    }

    public static Matcher<WaitlistResponse> region(String expected) {
        return new FeatureMatcher<>(is(expected), "region", "but region") {
            @Override
            protected String featureValueOf(WaitlistResponse actual) {
                return actual.region();
            }
        };
    }
}
