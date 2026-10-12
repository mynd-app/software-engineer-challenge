<?php

namespace Database\Factories;

use App\Enums\Mood;
use App\Models\CheckIn;
use App\Models\User;
use Illuminate\Database\Eloquent\Factories\Factory;
use Illuminate\Support\Carbon;

/**
 * @extends Factory<CheckIn>
 */
class CheckInFactory extends Factory
{
    public function definition(): array
    {
        return [
            'user_id' => User::factory(),
            'mood' => fake()->randomElement(Mood::cases()),
            'intensity' => fake()->numberBetween(1, 5),
            'note' => null,
        ];
    }

    /**
     * A check-in made at a given local time in the user's timezone.
     */
    public function atLocal(string $localDateTime, string $timezone): static
    {
        $at = Carbon::parse($localDateTime, $timezone)->utc();

        return $this->state(['created_at' => $at, 'updated_at' => $at]);
    }
}
