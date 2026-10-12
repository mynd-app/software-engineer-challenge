<?php

namespace App\Support;

use App\Enums\CompanyFeature;
use App\Models\User;

class FeatureAccess
{
    /**
     * Whether this user currently has a company-gated feature.
     */
    public static function hasCompanyFeatureAccess(User $user, CompanyFeature $feature): bool
    {
        $company = $user->company;

        if ($company === null) {
            return false;
        }

        // An explicit per-user override always wins.
        $override = $user->getAttribute($feature->userOverrideColumn());
        if ($override !== null) {
            return (bool) $override;
        }

        if (! $company->getAttribute($feature->companySwitchColumn())) {
            return false;
        }

        return (bool) ($company->getAttribute($feature->companyUserDefaultColumn()) ?? true);
    }

    /**
     * @return array<string, bool>
     */
    public static function resolvedFor(User $user): array
    {
        $features = [];

        foreach (CompanyFeature::cases() as $feature) {
            $features[$feature->value] = self::hasCompanyFeatureAccess($user, $feature);
        }

        return $features;
    }
}
