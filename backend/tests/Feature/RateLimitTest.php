<?php

use App\Models\User;

it('throttles a user after 30 requests a minute', function () {
    $user = User::factory()->create();

    for ($i = 0; $i < 30; $i++) {
        apiAs($user, 'GET', '/check-ins')->assertOk();
    }

    apiAs($user, 'GET', '/check-ins')->assertTooManyRequests();
});
