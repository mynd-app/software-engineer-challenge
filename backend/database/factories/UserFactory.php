<?php

namespace Database\Factories;

use App\Enums\Role;
use App\Models\Company;
use App\Models\User;
use Illuminate\Database\Eloquent\Factories\Factory;
use Illuminate\Support\Str;

/**
 * @extends Factory<User>
 */
class UserFactory extends Factory
{
    public function definition(): array
    {
        return [
            'company_id' => Company::factory(),
            'name' => fake()->name(),
            'email' => fake()->unique()->safeEmail(),
            'team' => fake()->randomElement(['Engineering', 'Finance', 'Warehouse']),
            'role' => Role::Member,
            'timezone' => 'Australia/Melbourne',
            'api_token' => Str::random(40),
            'insights_enabled' => null,
        ];
    }

    public function hrAdmin(): static
    {
        return $this->state(['role' => Role::HrAdmin]);
    }

    public function withoutCompany(): static
    {
        return $this->state(['company_id' => null]);
    }
}
