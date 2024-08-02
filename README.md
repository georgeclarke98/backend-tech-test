# Ffern Backend / Full Stack Tech Test

This repository contains the backend technical test for software engineers at Ffern.

## The problem

Ffern manages a waiting list of prospective customers. The systems setup to manage the waiting list is not fit for purpose. The waiting list needs to be re-implemented.

Your task is to architect and implement the new waiting list system.

There are two components required for this test:

(A) A waiting list database 

(B) API endpoints required

The user flow is as follows:

1. User signs up on the waiting list on the Ffern website.
2. User is texted an opt in message.
3. User replies with their first name.
4. When places are available, a non-technical staff member makes a "trigger" set to a certain date, for a certain cohort.
5. When the trigger is activated, members of the relevant cohort are given the opportunity to join the ledger.
6. After 48 hours, that opportunity is retracted.

Some further points to note:

- Analytics data for users is sent from the website, and must be stored for analysis.
- Triggers will specify regions (e.g., EU/UK/US).
- Users may opt out of the waiting list at any point.
- The staff member creating triggers may be non-technical. A process for this is not required for this test, but a plan for its implementation is desirable.

## Technical Requirements:

Postgres / PostgreSQL should be used, ideally deployed via Supabase.io.

API endpoints should be serverless. A simple Vercel API is acceptable.

If time permits, a simple Next.js frontend could be implemented, containing a phone input that triggers the sign up flow.

All endpoints should be adequetly authenticated.
