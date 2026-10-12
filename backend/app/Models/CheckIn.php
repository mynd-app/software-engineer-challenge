<?php

namespace App\Models;

use App\Enums\Mood;
use Database\Factories\CheckInFactory;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Support\Carbon;

/**
 * @property int $id
 * @property int $user_id
 * @property Mood $mood
 * @property int $intensity
 * @property string|null $note
 * @property Carbon $created_at
 * @property Carbon $updated_at
 * @property-read User $user
 */
class CheckIn extends Model
{
    /** @use HasFactory<CheckInFactory> */
    use HasFactory;

    protected $fillable = [
        'user_id',
        'mood',
        'intensity',
        'note',
    ];

    protected function casts(): array
    {
        return [
            'mood' => Mood::class,
            'intensity' => 'integer',
        ];
    }

    /**
     * @return BelongsTo<User, $this>
     */
    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class);
    }
}
