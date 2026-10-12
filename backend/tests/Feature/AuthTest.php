<?php

use App\Models\User;

it('rejects requests without a token', function () {
    $this->getJson('/api/check-ins')
        ->assertUnauthorized()
        ->assertExactJson(['message' => 'Unauthenticated.']);
});

it('rejects an unknown token', function () {
    $this->withHeader('X-Mynd-Token', 'not-a-real-token')
        ->getJson('/api/check-ins')
        ->assertUnauthorized();
});

it('accepts a valid token', function () {
    $user = User::factory()->create();

    apiAs($user, 'GET', '/check-ins')->assertOk();
});
