package com.example.ffern.users.matchers;

import com.example.ffern.users.json.UserAnalyticsResponse;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.Map;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class UserAnalyticsResponseMatchers {

    @SafeVarargs
    public static Matcher<UserAnalyticsResponse> userAnalyticsResponse(Matcher<UserAnalyticsResponse>... matchers) {
        return allOf(matchers);
    }

    public static Matcher<UserAnalyticsResponse> id(Long expected) {
        return new FeatureMatcher<>(is(expected), "id", "but id") {
            @Override
            protected Long featureValueOf(UserAnalyticsResponse actual) {
                return actual.id();
            }
        };
    }

    public static Matcher<UserAnalyticsResponse> userId(Long expected) {
        return new FeatureMatcher<>(is(expected), "userId", "but userId") {
            @Override
            protected Long featureValueOf(UserAnalyticsResponse actual) {
                return actual.userId();
            }
        };
    }

    @SafeVarargs
    public static Matcher<UserAnalyticsResponse> data(Matcher<Map<? extends String, ?>> ... matchers) {
        return new FeatureMatcher<>(allOf(matchers), "data", "but data") {
            @Override
            protected Map<String, Object> featureValueOf(UserAnalyticsResponse actual) {
                return actual.data();
            }
        };
    }


}
