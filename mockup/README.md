# mytop100 — Clickable Prototype (Phase 2)

> ⚠ **Prototype — not production code.** This is a self-contained HTML mockup
> derived from [`../specification/specs.md`](../specification/specs.md).
> Open `index.html` directly in any browser (no server, no build, no
> dependencies). All navigation and interactions are functional with mock data
> standing in for the external music API.

## How to run

Double-click `mockup/index.html`, or once deployed visit the GitHub Pages URL.
Start at the **Login** screen and pick any provider to enter the app.

## Screens

| # | Screen | Route (in-app anchor) | Status |
|---|--------|-----------------------|--------|
| 1 | Login / Sign-in (Google, GitHub, Instagram, X) | `#view-login` | Draft |
| 2 | My Top 100 (drag-to-reorder, draft/publish toggle, external link, remove) | `#view-mylist` | Draft |
| 3 | Song search / Add song (modal, enforces one-song-per-artist + 100 cap) | `#search-modal` | Draft |
| 4 | Discover / Follow users (open follow, published vs. draft states) | `#view-discover` | Draft |
| 5 | User profile / Their Top 100 (published lists only) | `#view-profile` | Draft |
| 6 | Compare (overlap %, shared songs/artists, ranking gap, side-by-side) | `#view-compare` | Draft |
| 7 | Statistics / Fun facts (oldest, newest, avg year, decade & genre bars) | `#view-stats` | Draft |

## Interactions demonstrated

- **Drag-to-reorder** the top 100 via the ⠿ handle; ranks renumber live. Works
  on both **touch (mobile)** and **mouse (desktop)** via Pointer Events, with
  edge auto-scroll for long lists.
- **One song per artist** rule enforced in search (blocked entries show a `1 / artist` badge; adding a taken artist shows an error toast).
- **100-song hard cap** enforced on add.
- **Draft ⇄ Published** toggle; draft lists are hidden from others; published lists update live.
- **Follow / unfollow** users; only **published** lists are viewable/comparable.
- **Compare** computes overlap %, shared songs, shared artists, and biggest ranking gap.
- **Stats** auto-computed: oldest/newest song, average & most-common decade, decade distribution, genre breakdown, and a fun fact.

## Notes / mock data

- The song catalog and other users' lists are hard-coded stand-ins for the
  external music API and real accounts.
- "▶" opens a search on a music service in a new tab (placeholder for the
  per-entry external link).
- Not yet resolved (deferred to Phase 3): the real music API choice and how
  artist identity (solo vs. group) is keyed — see the spec's Open Questions.
