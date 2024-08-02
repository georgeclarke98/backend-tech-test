# Ffern Backend / Full Stack Tech Test

This repository contains the backend technical test for software engineers at Ffern.

## What we are testing

(1) How do you approach system design? What choices do you make that reflect system architecture virtues such as scalability, simplicity, and robustness.

(2) What models do you choose to represent resources? Are these appropriate for the represented entities?

(3) How full stack are you?

(4) How do you approach access controls (authentication / authorisation)?

(5) How do you approach testing?

## The problem

Ffern manages a waiting list of prospective customers. The systems setup to manage the waiting list is not fit for purpose. The waiting list needs to be re-implemented.

Your task is to architect and implement the new waiting list system.

There are three components required for this test:

(A) A waiting list database 

(B) API endpoints

(C) Sign up page

The user flow is as follows:

1. User signs up on the waiting list on the website. They give (a) their phone number, and (b) their name.
2. When places are available, a non-technical staff member makes a "trigger" set to a certain date, for a certain cohort.
3. When the trigger is activated, members of the relevant cohort are given the opportunity to join the ledger.
4. After 48 hours, that opportunity is retracted.

Some further points to note:

- Analytics data for users is sent from the website, and must be stored for analysis. Analytics will be sent as a JSON object with an unknown structure.
- Triggers will specify regions (e.g., EU/UK/US).
- Users may opt out of the waiting list at any point.
- The staff member creating triggers may be non-technical. A process for creating a trigger is not required, but a plan for its implementation is desirable.

## Technical Requirements:

Postgres / PostgreSQL should be used, ideally deployed via Supabase.io.

API endpoints should be serverless. A simple Vercel / Next.js API is acceptable.

For the frontend, a Next.js application should be created. This can be a single page with a signup form. Don't fret over the UI / design.

All endpoints should be adequetly authenticated. Request and response payloads should be adequetly validated.
