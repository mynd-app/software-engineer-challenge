<?php

namespace App\Http\Requests;

use App\Enums\Mood;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class StoreCheckInRequest extends FormRequest
{
    /**
     * @return array<string, mixed>
     */
    public function rules(): array
    {
        return [
            'mood' => ['required', Rule::enum(Mood::class)],
            'intensity' => ['required', 'integer', 'between:1,5'],
            'note' => ['nullable', 'string', 'max:1000'],
        ];
    }
}
