<?php

namespace App\Http\Resources;

use App\Models\CheckIn;
use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

/**
 * @mixin CheckIn
 */
class CheckInResource extends JsonResource
{
    /**
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
            'id' => $this->id,
            'mood' => $this->mood->value,
            'intensity' => $this->intensity,
            'note' => $this->note,
            'created_at' => $this->created_at->copy()->utc()->format('Y-m-d\TH:i:s\Z'),
        ];
    }
}
