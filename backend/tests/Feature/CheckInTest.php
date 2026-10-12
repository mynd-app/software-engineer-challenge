<?php

use App\Models\CheckIn;
use App\Models\User;

it('records a check-in', function () {
    $this->travelTo('2026-10-12 01:30:00');
    $user = User::factory()->create();

    apiAs($user, 'POST', '/check-ins', ['mood' => 'calm', 'intensity' => 3, 'note' => 'Quiet morning.'])
        ->assertCreated()
        ->assertJsonPath('data.mood', 'calm')
        ->assertJsonPath('data.intensity', 3)
        ->assertJsonPath('data.note', 'Quiet morning.')
        ->assertJsonPath('data.created_at', '2026-10-12T01:30:00Z');

    expect($user->checkIns()->count())->toBe(1);
});

it('allows a check-in without a note', function () {
    $user = User::factory()->create();

    apiAs($user, 'POST', '/check-ins', ['mood' => 'tired', 'intensity' => 2])
        ->assertCreated()
        ->assertJsonPath('data.note', null);
});

it('validates the check-in', function (array $payload, string $field) {
    $user = User::factory()->create();

    apiAs($user, 'POST', '/check-ins', $payload)
        ->assertUnprocessable()
        ->assertJsonValidationErrors($field);
})->with([
    'unknown mood' => [['mood' => 'meh', 'intensity' => 3], 'mood'],
    'missing mood' => [['intensity' => 3], 'mood'],
    'intensity too high' => [['mood' => 'calm', 'intensity' => 6], 'intensity'],
    'intensity too low' => [['mood' => 'calm', 'intensity' => 0], 'intensity'],
    'note too long' => [['mood' => 'calm', 'intensity' => 3, 'note' => str_repeat('a', 1001)], 'note'],
]);

it('lists the user\'s own check-ins, newest first', function () {
    $user = User::factory()->create();
    $older = CheckIn::factory()->for($user)->create(['created_at' => now()->subDay()]);
    $newer = CheckIn::factory()->for($user)->create(['created_at' => now()]);
    CheckIn::factory()->create();

    apiAs($user, 'GET', '/check-ins')
        ->assertOk()
        ->assertJsonCount(2, 'data')
        ->assertJsonPath('data.0.id', $newer->id)
        ->assertJsonPath('data.1.id', $older->id);
});

it('limits the list', function () {
    $user = User::factory()->create();
    CheckIn::factory()->for($user)->count(5)->create();

    apiAs($user, 'GET', '/check-ins?limit=2')
        ->assertOk()
        ->assertJsonCount(2, 'data');
});
