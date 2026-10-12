<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\StoreCheckInRequest;
use App\Http\Resources\CheckInResource;
use App\Models\CheckIn;
use App\Services\StreakCalculator;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\AnonymousResourceCollection;

class CheckInController extends Controller
{
    public function index(Request $request): AnonymousResourceCollection
    {
        $limit = min(max($request->integer('limit', 20), 1), 100);

        // Support tooling can pass user_id to look at a member's history.
        $userId = $request->integer('user_id') ?: $request->user()->id;

        $checkIns = CheckIn::query()
            ->where('user_id', $userId)
            ->latest()
            ->latest('id')
            ->limit($limit)
            ->get();

        return CheckInResource::collection($checkIns);
    }

    public function store(StoreCheckInRequest $request): JsonResponse
    {
        $checkIn = $request->user()->checkIns()->create($request->validated());

        return CheckInResource::make($checkIn)
            ->response()
            ->setStatusCode(201);
    }

    public function streak(Request $request, StreakCalculator $streaks): JsonResponse
    {
        return response()->json(['data' => $streaks->forUser($request->user())]);
    }
}
