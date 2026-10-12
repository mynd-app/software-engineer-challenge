<?php

namespace App\Enums;

enum Mood: string
{
    case Happy = 'happy';
    case Calm = 'calm';
    case Grateful = 'grateful';
    case Energised = 'energised';
    case Tired = 'tired';
    case Anxious = 'anxious';
    case Stressed = 'stressed';
    case Sad = 'sad';
    case Angry = 'angry';
    case Lonely = 'lonely';

    public function isPositive(): bool
    {
        return in_array($this, [self::Happy, self::Calm, self::Grateful, self::Energised], true);
    }
}
