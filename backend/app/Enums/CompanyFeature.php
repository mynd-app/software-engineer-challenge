<?php

namespace App\Enums;

/**
 * Features a company switches on for its people. Each one has three columns:
 * the company master switch, the company default for members, and a nullable
 * per-user override.
 */
enum CompanyFeature: string
{
    case Insights = 'insights';

    public function companySwitchColumn(): string
    {
        return "{$this->value}_enabled";
    }

    public function companyUserDefaultColumn(): string
    {
        return "{$this->value}_user_default";
    }

    public function userOverrideColumn(): string
    {
        return "{$this->value}_enabled";
    }
}
