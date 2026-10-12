<?php

use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Illuminate\Testing\TestResponse;
use Tests\TestCase;

pest()->extend(TestCase::class)
    ->use(RefreshDatabase::class)
    ->in('Feature');

/**
 * Call the API as the given user.
 *
 * @param  array<string, mixed>  $data
 */
function apiAs(User $user, string $method, string $uri, array $data = []): TestResponse
{
    // Each call is a fresh request, as it would be over HTTP: don't let the
    // user resolved by a previous request leak into this one.
    app('auth')->forgetGuards();

    return test()->withHeader('X-Mynd-Token', $user->api_token)
        ->json($method, "/api{$uri}", $data);
}
