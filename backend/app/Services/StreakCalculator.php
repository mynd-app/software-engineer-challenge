<?php

namespace App\Services;

use App\Models\User;
use Illuminate\Support\Carbon;

class StreakCalculator
{
    /**
     * Consecutive days with at least one check-in, ending today or yesterday.
     *
     * @return array{current_streak_days: int, last_check_in_date: string|null}
     */
    public function forUser(User $user): array
    {
        $days = $user->checkIns()
            ->latest()
            ->pluck('created_at')
            ->map(fn (Carbon $createdAt) => $createdAt->toDateString())
            ->unique()
            ->values();

        if ($days->isEmpty()) {
            return ['current_streak_days' => 0, 'last_check_in_date' => null];
        }

        $today = Carbon::now()->startOfDay();
        $cursor = Carbon::parse($days->first());

        // A streak survives until the end of the day after the last check-in.
        if ($cursor->lt($today->copy()->subDay())) {
            return ['current_streak_days' => 0, 'last_check_in_date' => $days->first()];
        }

        $streak = 0;
        foreach ($days as $day) {
            if ($day !== $cursor->toDateString()) {
                break;
            }

            $streak++;
            $cursor->subDay();
        }

        return ['current_streak_days' => $streak, 'last_check_in_date' => $days->first()];
    }
}
