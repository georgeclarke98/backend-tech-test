CREATE SEQUENCE users_seq;
CREATE SEQUENCE waitlist_seq;
CREATE SEQUENCE user_analytics_seq;
CREATE SEQUENCE triggers_seq;

CREATE TABLE users
(
    id           SERIAL PRIMARY KEY NOT NULL,
    first_name   VARCHAR(64)        NOT NULL,
    last_name    VARCHAR(64)        NOT NULL,
    email        VARCHAR(128)       NOT NULL,
    phone_number VARCHAR(16)        NOT NULL,
    opt_in       BOOLEAN            NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP          NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP          NOT NULL DEFAULT NOW()
);

CREATE TABLE waitlist
(
    id         SERIAL PRIMARY KEY NOT NULL,
    user_id    SERIAL             NOT NULL REFERENCES users (id),
    region     VARCHAR(4)         NOT NULL,
    cohort     VARCHAR(16)        NOT NULL,
    created_at TIMESTAMP          NOT NULL DEFAULT NOW()
);

CREATE TABLE user_analytics
(
    id         SERIAL PRIMARY KEY NOT NULL,
    user_id    SERIAL             NOT NULL REFERENCES users (id),
    data       JSON               NOT NULL,
    created_at TIMESTAMP          NOT NULL DEFAULT NOW()
);

CREATE TABLE triggers
(
    id           SERIAL PRIMARY KEY NOT NULL,
    region       VARCHAR(4)         NOT NULL,
    cohort       VARCHAR(16)        NOT NULL,
    trigger_date TIMESTAMP          NOT NULL,
    status       INT                NOT NULL DEFAULT 0,
    created_at   TIMESTAMP          NOT NULL DEFAULT NOW(),
    created_by   VARCHAR(64)        NOT NULL,
    updated_at   TIMESTAMP          NOT NULL DEFAULT NOW(),
    updated_by   VARCHAR(64)        NOT NULL
);

CREATE INDEX idx_waitlist_region_cohort ON waitlist (region, cohort);
