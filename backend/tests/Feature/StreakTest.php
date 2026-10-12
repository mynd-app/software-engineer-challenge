<?php

use App\Models\CheckIn;
use App\Models\User;
use Illuminate\Support\Carbon;

it('returns an empty streak for a new user', function () {
    $user = User::factory()->create();

    apiAs($user, 'GET', '/check-ins/streak')
        ->assertOk()
        ->assertExactJson(['data' => ['current_streak_days' => 0, 'last_check_in_date' => null]]);
});

it('counts consecutive days', function () {
    $this->travelTo('2026-10-12 12:00:00');
    $user = User::factory()->create(['timezone' => 'UTC']);

    foreach (['2026-10-10 09:00', '2026-10-11 09:00', '2026-10-11 18:00', '2026-10-12 08:00'] as $at) {
        CheckIn::factory()->for($user)->atLocal($at, 'UTC')->create();
    }

    apiAs($user, 'GET', '/check-ins/streak')
        ->assertOk()
        ->assertJsonPath('data.current_streak_days', 3)
        ->assertJsonPath('data.last_check_in_date', '2026-10-12');
});

it('keeps the streak alive until the end of the next day', function () {
    $this->travelTo('2026-10-12 20:00:00');
    $user = User::factory()->create(['timezone' => 'UTC']);

    CheckIn::factory()->for($user)->atLocal('2026-10-10 09:00', 'UTC')->create();
    CheckIn::factory()->for($user)->atLocal('2026-10-11 09:00', 'UTC')->create();

    apiAs($user, 'GET', '/check-ins/streak')
        ->assertJsonPath('data.current_streak_days', 2);
});

it('resets the streak after a missed day', function () {
    $this->travelTo('2026-10-12 12:00:00');
    $user = User::factory()->create(['timezone' => 'UTC']);

    CheckIn::factory()->for($user)->atLocal('2026-10-09 09:00', 'UTC')->create();
    CheckIn::factory()->for($user)->atLocal('2026-10-10 09:00', 'UTC')->create();

    apiAs($user, 'GET', '/check-ins/streak')
        ->assertJsonPath('data.current_streak_days', 0)
        ->assertJsonPath('data.last_check_in_date', '2026-10-10');
});

it('counts days in the user\'s timezone', function () {
    // 8am Monday in Melbourne is still Sunday evening in UTC. Early-morning and late-night
    // check-ins land on different UTC days than the days the user actually lived.
    $this->travelTo(Carbon::parse('2026-10-12 08:00', 'Australia/Melbourne'));
    $user = User::factory()->create(['timezone' => 'Australia/Melbourne']);

    CheckIn::factory()->for($user)->atLocal('2026-10-10 00:30', 'Australia/Melbourne')->create();
    CheckIn::factory()->for($user)->atLocal('2026-10-11 23:30', 'Australia/Melbourne')->create();
    CheckIn::factory()->for($user)->atLocal('2026-10-12 07:30', 'Australia/Melbourne')->create();

    apiAs($user, 'GET', '/check-ins/streak')
        ->assertJsonPath('data.current_streak_days', 3)
        ->assertJsonPath('data.last_check_in_date', '2026-10-12');
});
