# Insights challenge API

Laravel 13 / PHP 8.5 / SQLite. Runs in Docker; you don't need PHP installed locally.

## Setup (not part of your 4 hours)

```sh
docker compose up -d --build          # first boot runs composer install (a few minutes), then serves :8080
docker compose logs -f app            # wait for "Server running on", then Ctrl+C
docker compose exec app composer setup   # .env, app key, SQLite database, migrate + seed
```

Check it works:

```sh
curl -s http://localhost:8080/api/config -H 'X-Mynd-Token: member-token'
```

## Everyday commands

```sh
docker compose exec app composer test      # Pest
docker compose exec app composer lint      # Pint (formatting)
docker compose exec app composer phpstan   # Larastan, level 5
docker compose exec app php artisan migrate:fresh --seed   # reset the demo data
```

## Seeded data

One company, Northwind Freight, with 44 people in five teams of very different sizes
(Leadership 3, Finance 4, Engineering 9, Customer Support 14, Warehouse 14), about ten weeks of
check-ins, and people in Melbourne, Perth and Auckland. A second company, Bluegum Studio, has two
people and insights switched off.

| Token | Who |
|---|---|
| `admin-token` | HR admin at Northwind Freight (Leadership), insights on |
| `member-token` | Engineering member at Northwind Freight |
| `perth-token` | Warehouse member at Northwind Freight, timezone Australia/Perth |
| `other-company-token` | Member of Bluegum Studio |

Send the token in the `X-Mynd-Token` header.

## API

All responses are JSON wrapped in `{"data": ...}`, with snake_case keys and UTC ISO-8601
timestamps.

| Method | Path | Notes |
|---|---|---|
| GET | `/api/config` | Feature flags resolved for the caller |
| POST | `/api/check-ins` | `{"mood": "calm", "intensity": 1-5, "note": "optional"}` |
| GET | `/api/check-ins` | The caller's check-ins, newest first, `?limit=` (default 20) |
| GET | `/api/check-ins/streak` | Consecutive days with a check-in, in the user's timezone |

Moods: `happy, calm, grateful, energised, tired, anxious, stressed, sad, angry, lonely`.

## Company-gated features

`App\Support\FeatureAccess::hasCompanyFeatureAccess()` decides whether a user has a feature such
as insights. A company has a master switch (`insights_enabled`) and a default for its people
(`insights_user_default`); each user has a nullable override (`users.insights_enabled`, NULL means
"use the company default"). Access must fail closed: when in doubt, the answer is no.
