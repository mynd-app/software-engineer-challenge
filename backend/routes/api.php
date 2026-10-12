<?php

use App\Http\Controllers\Api\CheckInController;
use App\Http\Controllers\Api\ConfigController;
use Illuminate\Support\Facades\Route;

Route::middleware(['throttle:api', 'auth.token'])->group(function () {
    Route::get('/config', ConfigController::class);

    Route::get('/check-ins', [CheckInController::class, 'index']);
    Route::post('/check-ins', [CheckInController::class, 'store']);
    Route::get('/check-ins/streak', [CheckInController::class, 'streak']);
});
