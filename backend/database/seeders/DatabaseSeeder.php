<?php

namespace Database\Seeders;

use App\Enums\Mood;
use App\Enums\Role;
use App\Models\CheckIn;
use App\Models\Company;
use App\Models\User;
use Illuminate\Database\Seeder;
use Illuminate\Support\Carbon;
use Illuminate\Support\Str;

/**
 * Deterministic demo data: the same people and check-in pattern on every run
 * (dates are relative to today).
 *
 * Tokens for trying the API:
 *   admin-token          HR admin at Northwind Freight (Leadership)
 *   member-token         Engineering member at Northwind Freight
 *   perth-token          Warehouse member at Northwind Freight, in Perth
 *   other-company-token  Member of Bluegum Studio (insights off)
 */
class DatabaseSeeder extends Seeder
{
    private const TEAMS = [
        'Leadership' => 3,
        'Finance' => 4,
        'Engineering' => 9,
        'Customer Support' => 14,
        'Warehouse' => 14,
    ];

    private const FIRST_NAMES = [
        'Aisha', 'Ben', 'Chloe', 'Daniel', 'Elena', 'Farid', 'Grace', 'Hamish', 'Isla', 'Jack',
        'Keira', 'Liam', 'Mei', 'Noah', 'Olivia', 'Priya', 'Quinn', 'Ravi', 'Sophie', 'Tom',
        'Uma', 'Vince', 'Willow', 'Xavier', 'Yasmin', 'Zac', 'Amara', 'Blake', 'Cora', 'Dev',
        'Esme', 'Finn', 'Gemma', 'Hugo', 'Ivy', 'Jonah', 'Kai', 'Lucy', 'Marco', 'Nina',
        'Oscar', 'Pippa', 'Rory', 'Sana',
    ];

    private const LAST_NAMES = [
        'Nguyen', 'Smith', 'Patel', 'Williams', 'Chen', 'Brown', 'Kaur', 'Jones', 'Singh', 'Taylor',
        'Wilson', 'Kim', 'Martin', 'Ali', 'Anderson', 'Lee', 'Thompson', 'Rossi', 'White', 'Murphy',
    ];

    private const NOTES = [
        'Long day on the floor.',
        'Good chat with my manager.',
        'Did not sleep well.',
        'Deadline is stressing me out.',
        'Feeling on top of things this week.',
        'Back after a few days off.',
        'Rough shift, lots of complaints.',
        'Went for a walk at lunch, helped.',
        'Worried about the restructure.',
        'Kids were sick last night.',
    ];

    public function run(): void
    {
        mt_srand(20261012);

        $northwind = Company::query()->create([
            'name' => 'Northwind Freight',
            'insights_enabled' => true,
            'insights_user_default' => false,
        ]);

        $people = $this->seedNorthwind($northwind);

        $bluegum = Company::query()->create([
            'name' => 'Bluegum Studio',
            'insights_enabled' => false,
            'insights_user_default' => null,
        ]);

        $people[] = User::query()->create([
            'company_id' => $bluegum->id,
            'name' => 'Rhea Collins',
            'email' => 'rhea@bluegum.example',
            'team' => 'Design',
            'timezone' => 'Australia/Sydney',
            'api_token' => 'other-company-token',
        ]);
        $people[] = User::query()->create([
            'company_id' => $bluegum->id,
            'name' => 'Sam Okafor',
            'email' => 'sam@bluegum.example',
            'team' => 'Design',
            'timezone' => 'Australia/Sydney',
            'api_token' => 'bluegum-2-token',
        ]);

        foreach ($people as $user) {
            $this->seedCheckIns($user);
        }
    }

    /**
     * @return list<User>
     */
    private function seedNorthwind(Company $company): array
    {
        $people = [];
        $n = 0;

        foreach (self::TEAMS as $team => $size) {
            for ($i = 0; $i < $size; $i++) {
                $first = self::FIRST_NAMES[$n % count(self::FIRST_NAMES)];
                $last = self::LAST_NAMES[($n * 7) % count(self::LAST_NAMES)];

                $attributes = [
                    'company_id' => $company->id,
                    'name' => "{$first} {$last}",
                    'email' => Str::lower("{$first}.{$last}.{$n}@northwind.example"),
                    'team' => $team,
                    'role' => Role::Member,
                    'timezone' => $this->timezoneFor($team, $i),
                    'api_token' => "northwind-{$n}-token",
                ];

                if ($team === 'Leadership' && $i === 0) {
                    $attributes['role'] = Role::HrAdmin;
                    $attributes['api_token'] = 'admin-token';
                    $attributes['insights_enabled'] = true;
                }

                if ($team === 'Engineering' && $i === 0) {
                    $attributes['api_token'] = 'member-token';
                }

                if ($team === 'Warehouse' && $i === 0) {
                    $attributes['api_token'] = 'perth-token';
                }

                $people[] = User::query()->create($attributes);
                $n++;
            }
        }

        return $people;
    }

    private function timezoneFor(string $team, int $index): string
    {
        return match (true) {
            $team === 'Warehouse' && $index < 5 => 'Australia/Perth',
            $team === 'Customer Support' && $index < 2 => 'Pacific/Auckland',
            default => 'Australia/Melbourne',
        };
    }

    private function seedCheckIns(User $user): void
    {
        $today = Carbon::now($user->timezone)->startOfDay();
        $alwaysOn = in_array($user->api_token, ['member-token', 'admin-token'], true);
        $engagement = $alwaysOn ? 100 : mt_rand(15, 80);

        for ($daysAgo = 70; $daysAgo >= 0; $daysAgo--) {
            $isRecent = $daysAgo <= 6;

            if (! ($alwaysOn && $isRecent) && mt_rand(1, 100) > $engagement) {
                continue;
            }

            $localTime = $today->copy()
                ->subDays($daysAgo)
                ->setTime(mt_rand(7, 21), mt_rand(0, 59));

            if ($localTime->isFuture()) {
                $localTime = Carbon::now($user->timezone)->subMinutes(mt_rand(5, 120));
            }

            $mood = $this->pickMood($user->team, $daysAgo);
            $at = $localTime->copy()->utc();

            CheckIn::query()->forceCreate([
                'user_id' => $user->id,
                'mood' => $mood,
                'intensity' => mt_rand(1, 5),
                'note' => mt_rand(1, 100) <= 20 ? self::NOTES[mt_rand(0, count(self::NOTES) - 1)] : null,
                'created_at' => $at,
                'updated_at' => $at,
            ]);
        }
    }

    /**
     * Customer Support has had a hard few weeks; everyone else is fairly steady.
     */
    private function pickMood(?string $team, int $daysAgo): Mood
    {
        $stressedSpell = $team === 'Customer Support' && $daysAgo <= 21;
        $negativeChance = $stressedSpell ? 65 : 30;

        $pool = mt_rand(1, 100) <= $negativeChance
            ? [Mood::Tired, Mood::Anxious, Mood::Stressed, Mood::Sad, Mood::Angry, Mood::Lonely]
            : [Mood::Happy, Mood::Calm, Mood::Grateful, Mood::Energised];

        return $pool[mt_rand(0, count($pool) - 1)];
    }
}
