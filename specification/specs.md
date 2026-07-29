# Project Specification

> This file is owned by the human. Use the `refine-specs` skill to iterate with the agent.
> Sections marked `[CONFIRMED]` have been reviewed and agreed upon.

---

## Project Summary [CONFIRMED]

**mytop100** is a personal web application for ranking your top 100 songs. Each
user builds a single ordered list of up to 100 songs, subject to one hard rule:
**only one song per artist**. Users search for songs via an external music API,
add them to their list (each entry links back to the song on the external
service), reorder the list by dragging, and view song details (title, artist,
style, year). A user's ranking is stored in the app's own database. The app has
a social dimension: users log in via social accounts, follow friends, compare
their top 100 lists, and see statistics and fun facts. Lists start as private
"drafts" and can later be published for followers to see.

---

## Actors [CONFIRMED]

| Actor | Role |
|-------|------|
| Authenticated user | Builds, ranks, and manages their own top 100; follows other users; compares lists; controls their list's visibility (draft/published). |
| Follower | An authenticated user who follows another user and can view that user's published list and comparisons. |

---

## Use Cases / User Stories [CONFIRMED]

**As a** user, **I want to** log in using my Google, GitHub, Instagram, or X account **so that** I don't need to manage a separate password.

**As a** user, **I want to** search for songs via an external music catalog **so that** I can quickly find and add the ones I want.

**As a** user, **I want to** add a song to my top 100 (with a link back to the song on the external service) **so that** I can build my list and play/reference the original.

**As a** user, **I want to** be prevented from adding more than one song per artist **so that** my list respects the one-song-per-artist rule.

**As a** user, **I want to** reorder my top 100 by sliding/dragging songs **so that** I can rank them in my preferred order.

**As a** user, **I want to** see each song's title, artist, style, and year of release **so that** I have the key details at a glance.

**As a** user, **I want to** keep my top 100 as a private "draft" **so that** it is not visible to others until I choose to publish it.

**As a** user, **I want to** publish my top 100 **so that** my followers can see it, and my edits are reflected dynamically.

**As a** user, **I want to** follow other users **so that** I can see and compare their lists.

**As a** user, **I want to** compare my top 100 with another user's **so that** I can see how our tastes align and differ.

**As a** user, **I want to** see statistics and funny facts about my list (and comparisons with others) **so that** the experience is more engaging.

---

## Screens / Views [CONFIRMED]

- **Login / Sign-in:** social login options (Google, GitHub, Instagram, X).
- **My Top 100:** the user's ranked list with drag-to-reorder; shows title, artist, style, year, and an external link per song; draft/published status toggle.
- **Song search / Add song:** search interface backed by an external music API, enforcing the one-song-per-artist rule.
- **Discover / Follow users:** find and follow other users.
- **User profile / Their Top 100:** view a followed user's published list.
- **Compare:** side-by-side comparison of the user's top 100 with another user's.
- **Statistics / Fun facts:** stats and funny facts about the user's list and comparisons.

---

## Acceptance Criteria [CONFIRMED]

- A user can authenticate via Google, GitHub, Instagram, or X.
- A user can search for songs through an external music API and add them to their top 100.
- Each song entry displays title, artist, style, and year, and links to the song on the external service.
- The system prevents more than one song per artist from being added to a list.
- The list is capped at a hard maximum of 100 songs; a user may have fewer than 100.
- A user can reorder their top 100 via drag-and-drop (sliding).
- A list can be kept as a private draft (invisible to others) and later published.
- A published list reflects the owner's edits dynamically for followers.
- A user can follow other users and compare top 100 lists.
- Statistics and fun facts are shown for a user's list and for comparisons, including: song-overlap percentage, oldest song, newest song, decade distribution, style/genre breakdown, shared-artists count (with biggest ranking gaps on shared songs), and average / most-common release year.
- The app works as a responsive web app on both mobile and desktop.

---

## Non-Functional Requirements

- **Platform:** Responsive web app supporting both mobile web and desktop. No native mobile app in this version (possible future work).
- **Authentication:** Social login / OAuth via Google, GitHub, Instagram, and X; no self-managed passwords.
- **Data:** Song metadata and search come from an external music API; the app's own database persists each user's list, ranking, and social graph.
- **Privacy:** Draft lists must never be visible to any other user until explicitly published; only published lists can be viewed or compared.

> Performance, scalability, and accessibility targets are intentionally deferred to Phase 3 (Plan).

---

## Out of Scope [CONFIRMED]

- Native mobile applications (iOS/Android) — explicitly deferred to a possible later version.
- Multiple lists per user — each user has exactly one top 100.

---

## Resolved Decisions [CONFIRMED]

- **List size:** hard cap of 100 songs; fewer is allowed; a single list per user.
- **Song data:** external music API for search + metadata + link; internal DB for per-user ranking.
- **Style/genre:** a fixed taxonomy plus a free-text entry option.
- **Friend model:** open follow (one-directional, no approval needed), not mutual invite/accept.
- **Publish scope:** publishing exposes the list to all followers. A user can only view/compare lists that have been **published** (draft lists stay private regardless of follows).
- **Statistics & fun facts:** song-overlap percentage, oldest song, newest song, decade distribution, style/genre breakdown, shared-artists count (incl. biggest ranking gaps on shared songs), and average / most-common release year.
- **Providers:** Google, GitHub, Instagram, X.
- **Published lists are dynamic:** followers see edits reflected live.
- **One-per-artist rule:** the constraint is on a song's primary/credited artist.
  - A *featured* appearance does not consume that artist's slot (they can still
    have their own primary-artist song).
  - Solo work and group work are treated as **distinct artists**
    (e.g. John Lennon solo vs. The Beatles).

---

## Open Questions

- **Artist identity source (defer to Plan):** the one-per-artist rule (solo vs. group as distinct artists) depends on the external music API distinguishing artist entities. To resolve during Phase 3: which API, whether we key uniqueness on the API's artist ID, and how to handle songs credited to multiple primary artists. Not a blocker for the mockup.
