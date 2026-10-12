# AGENTS.md

Conventions for this repo, for people and for coding agents. `CLAUDE.md` imports this file.

## Product

- The product name is always **mynd**, all lowercase, in UI text, copy and docs.
- mynd handles mental health data. Treat check-ins, moods and especially notes as sensitive
  health information.

## Privacy and security rules

- **Never log personal or health data** (check-in notes, moods tied to a person, emails). Log ids.
- **The default filesystem disk in production is a PUBLIC S3 bucket.** It serves app media, so
  anything written to it is world-readable. Never write personal data with `Storage::put()`, or
  `->store()`, without naming a private disk (`local`).
- **Every endpoint authorises the caller.** Never trust an id from the request to decide whose
  data to return. A member only ever sees their own data.
- **Feature flags fail closed.** A missing, unknown or unresolvable flag means "off". Company-gated
  features resolve through `FeatureAccess::hasCompanyFeatureAccess()`, nowhere else.
- Employers never see an individual's data. Anything shown to an employer is aggregated and must
  not let them work out how one person feels.

## Backend (`backend/`)

- Laravel 13, PHP 8.5, SQLite, Pest. Everything runs in Docker:
  - `docker compose exec app composer test` - the test suite
  - `docker compose exec app vendor/bin/pint <paths>` - formatting
  - `docker compose exec app composer phpstan` - static analysis
- JSON responses use a `{"data": ...}` envelope, snake_case keys, and ISO-8601 UTC timestamps.
- Store times in UTC. Work out calendar dates ("today", a week) in the user's own timezone.
- Every endpoint gets Pest feature tests, including the unauthorised paths. Time-dependent tests
  pin the clock with `travelTo()`.

## Android (`android/`)

- Kotlin Multiplatform (Android target), Compose Multiplatform, Ktor, Koin.
- Business logic, networking, ViewModels and screens live in `shared/`. `androidApp/` is a thin
  shell.
- Reuse `MyndScaffold` (`shared/.../ui/components/`) for new screens. It's the one shared scaffold
  and top bar. Don't hand-roll a new one.
- Screens are routed with the `Screen` sealed interface in `App.kt`.
- Run the tests with `./gradlew :shared:testAndroidHostTest`.

## Working style

- Small commits with plain messages that say what changed.
- Read every diff an agent produces before you commit it. You own what ships.
- If something is ambiguous, make a reasonable call, write it down, and keep going.
