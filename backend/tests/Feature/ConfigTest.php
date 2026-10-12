<?php

use App\Models\Company;
use App\Models\User;

it('turns insights off for users without a company', function () {
    $user = User::factory()->withoutCompany()->create();

    apiAs($user, 'GET', '/config')
        ->assertOk()
        ->assertExactJson(['data' => ['features' => ['insights' => false]]]);
});

it('turns insights off when the company switch is off', function () {
    $user = User::factory()->for(Company::factory())->create();

    apiAs($user, 'GET', '/config')->assertJsonPath('data.features.insights', false);
});

it('uses the company default when the user has no override', function (bool $default) {
    $user = User::factory()->for(Company::factory()->withInsights($default))->create();

    apiAs($user, 'GET', '/config')->assertJsonPath('data.features.insights', $default);
})->with([true, false]);

it('lets a user override the company default', function (bool $override) {
    $user = User::factory()
        ->for(Company::factory()->withInsights(! $override))
        ->create(['insights_enabled' => $override]);

    apiAs($user, 'GET', '/config')->assertJsonPath('data.features.insights', $override);
})->with([true, false]);
